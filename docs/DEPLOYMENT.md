# Docker & AWS EC2 배포 가이드

## 개요
- 애플리케이션은 Spring Boot 3.x / JDK 21 환경에서 동작합니다.
- Docker 이미지는 `Dockerfile`을 사용해 빌드하며, 컨테이너 실행 시 `.env` 파일 또는 환경 변수를 통해 설정값을 주입합니다.
- 비밀 정보는 `.env`를 Git에 커밋하지 말고, AWS Systems Manager Parameter Store 또는 AWS Secrets Manager에 저장한 뒤 EC2에서 가져와 사용하세요.

## 1. 로컬 .env 구성
1. `.env.example`을 복사해 `.env`를 생성합니다.
   ```bash
   cp .env.example .env
   ```
2. 데이터베이스 접속 정보, `API_SECURITY_KEY` 등 민감한 값을 실제 값으로 수정합니다.
3. AWS Systems Manager Parameter Store 사용 시에는 `.env`에서 `AWS_SSM_ENABLED=true`, `AWS_SSM_PARAMETER_PATH=/findit/prod`(예시) 등을 지정해 두고, EC2 IAM Role 또는 자격 증명만 준비하면 애플리케이션이 자동으로 해당 경로의 파라미터를 불러옵니다.

Spring Boot는 `application.properties`의 `spring.config.import=optional:file:.env[.properties]` 설정을 통해 `.env` 파일을 자동으로 읽습니다.

> **참고**: 애플리케이션은 Flyway로 스키마를 관리합니다. 컨테이너가 처음 기동되면 `db/migration`에 있는 SQL이 자동으로 실행되어 테이블과 인덱스를 생성합니다.

## 2. Docker 이미지 빌드
1. 애플리케이션 JAR 빌드
   ```bash
   ./gradlew bootJar
   ```
2. Docker 이미지 생성
   ```bash
   docker build -t findit-server:latest .
   ```

## 3. Docker 컨테이너 실행 (EC2 포함)
### 3-1. .env 파일 사용
```bash
docker run -d \
  --name findit-server \
  --env-file .env \
  -p 8080:8080 \
  findit-server:latest
```

### 3-2. AWS Secrets Manager / Parameter Store 연동 예시
1. EC2 IAM Role 또는 AWS 자격 증명에 Parameter Store 읽기 권한을 부여합니다.
2. `.env` 또는 환경 변수에서 `AWS_SSM_ENABLED=true`, `AWS_SSM_PARAMETER_PATH=/findit/prod`, `AWS_SSM_REGION=ap-northeast-2` 등을 설정합니다.
3. 애플리케이션이 기동되면 지정된 경로 하위의 파라미터(`/findit/prod/db/url`, `/findit/prod/db/password` 등)가 자동으로 로드되어 Spring Property로 등록됩니다. 파라미터 이름은 슬래시(`/`) 대신 점(`.`)으로 변환되므로 `SPRING_DATASOURCE_URL` 대신 `/findit/prod/spring/datasource/url`처럼 계층 구조를 사용할 수 있습니다.

## 4. PostgreSQL 구성
- AWS RDS 또는 EC2 내 Docker 컨테이너로 PostgreSQL을 운영할 수 있습니다.
- Docker Compose를 사용하는 경우 다음과 같이 구성합니다.
  ```yaml
  services:
    db:
      image: postgres:15-alpine
      env_file:
        - .env
      environment:
        POSTGRES_DB: ${POSTGRES_DB}
        POSTGRES_USER: ${POSTGRES_USER}
        POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      volumes:
        - postgres-data:/var/lib/postgresql/data

    app:
      image: findit-server:latest
      env_file:
        - .env
      environment:
        SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/${POSTGRES_DB}
        SPRING_DATASOURCE_USERNAME: ${POSTGRES_USER}
        SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD}
      ports:
        - "8080:8080"
      depends_on:
        - db

  volumes:
    postgres-data:
  ```

## 5. 모니터링 및 헬스 체크 / 인증 적용
- `Actuator` 엔드포인트(`/actuator/health`)를 통해 애플리케이션 상태를 확인할 수 있습니다. 해당 엔드포인트는 인증 없이 접근 가능합니다.
- 일반 API 호출 시 `X-API-KEY` 헤더에 `API_SECURITY_KEY` 값을 넣어야 하며, 올바르지 않을 경우 401 응답을 반환합니다.
- Flyway가 자동으로 실행되므로 `spring.jpa.hibernate.ddl-auto=validate` 설정이 적용되어 있습니다. 수동으로 DDL을 실행할 필요가 없습니다.
- 경찰청 API 연동은 기본적으로 활성화되어 있으므로, Health Indicator에서 외부 API 상태를 확인할 수 있습니다. 장애가 발생하면 `POLICE_API_ENABLED=false`로 임시 비활성화할 수 있습니다.

## 6. 배포 체크리스트
- [ ] `.env` 또는 환경 변수를 최신 값으로 갱신했는가?
- [ ] `./gradlew bootJar`로 최신 JAR을 생성했는가?
- [ ] Docker 이미지 태그와 레지스트리(ECR 등)를 맞게 지정했는가?
- [ ] EC2 보안 그룹에서 8080 포트를 허용했는가?
- [ ] Actuator/기본 헬스 체크를 통해 정상 동작을 확인했는가?
- [ ] 기존 데이터베이스에 이미 테이블이 있다면, Flyway baseline을 한 번 수행(예: flyway CLI로 `baselineOnMigrate=true` 실행)했는가?
