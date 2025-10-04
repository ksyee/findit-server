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
2. 데이터베이스 접속 정보와 기타 환경 변수를 실제 값으로 수정합니다.

Spring Boot는 `application.properties`의 `spring.config.import=optional:file:.env[.properties]` 설정을 통해 `.env` 파일을 자동으로 읽습니다.

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
1. EC2에 AWS CLI를 설치하고 자격 증명을 구성합니다.
2. 비밀값을 불러와 환경 변수로 주입한 뒤 컨테이너를 실행합니다.
   ```bash
   export SPRING_DATASOURCE_PASSWORD=$(aws ssm get-parameter --name "/findit/db/password" --with-decryption --query Parameter.Value --output text)
   export SPRING_DATASOURCE_URL=$(aws ssm get-parameter --name "/findit/db/url" --with-decryption --query Parameter.Value --output text)
   export SPRING_DATASOURCE_USERNAME=$(aws ssm get-parameter --name "/findit/db/username" --with-decryption --query Parameter.Value --output text)
   export POLICE_API_ENABLED=false

   docker run -d \
     --name findit-server \
     -e SPRING_PROFILES_ACTIVE=docker \
     -e SPRING_DATASOURCE_URL \
     -e SPRING_DATASOURCE_USERNAME \
     -e SPRING_DATASOURCE_PASSWORD \
     -e POLICE_API_ENABLED \
     -p 8080:8080 \
     findit-server:latest
   ```

필요 시 스크립트를 작성해 매 배포 전에 자동으로 값을 받아올 수 있습니다.

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

## 5. 모니터링 및 헬스 체크
- `Actuator` 엔드포인트(`/actuator/health`)를 통해 애플리케이션 상태를 확인할 수 있습니다.
- 현재 경찰청 API는 비활성화 상태이므로, Health Indicator는 `Disabled` 상태를 보고합니다. 추후 API 복구 시 `POLICE_API_ENABLED=true`와 유효한 서비스 키를 설정하세요.

## 6. 배포 체크리스트
- [ ] `.env` 또는 환경 변수를 최신 값으로 갱신했는가?
- [ ] `./gradlew bootJar`로 최신 JAR을 생성했는가?
- [ ] Docker 이미지 태그와 레지스트리(ECR 등)를 맞게 지정했는가?
- [ ] EC2 보안 그룹에서 8080 포트를 허용했는가?
- [ ] Actuator/기본 헬스 체크를 통해 정상 동작을 확인했는가?
