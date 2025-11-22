# FindIt Server

**FindIt Server**는 분실물 및 습득물을 효율적으로 관리하기 위한 백엔드 애플리케이션입니다. Spring Boot를 기반으로 구축되었으며, 경찰청 유실물 통합포털(LOST112) 데이터와의 연동을 지원하여 더욱 풍부한 데이터를 제공합니다.

## 🛠 기술 스택 (Tech Stack)

- **Language**: Java 21
- **Framework**: Spring Boot 3.2.5
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Migration**: Flyway
- **Security**: Spring Security (API Key Authentication)
- **Documentation**: Swagger (SpringDoc)
- **Monitoring**: Spring Boot Actuator, Micrometer
- **Cloud & DevOps**: AWS SSM (Parameter Store), Docker

## ✨ 주요 기능 (Key Features)

- **분실물/습득물 관리**: 분실물 및 습득물 등록, 조회, 검색 API 제공
- **경찰청 API 연동**: 외부 경찰청 데이터베이스와 연동하여 데이터 동기화 (Feature Flag로 제어 가능)
- **보안 인증**: API Key 기반의 인증 시스템 적용
- **배포 친화적**: Docker 및 AWS 배포를 고려한 유연한 설정 (`.env`, AWS Parameter Store 연동)
- **모니터링**: Actuator 및 Prometheus 연동을 통한 애플리케이션 상태 모니터링

## 🚀 시작하기 (Getting Started)

### 전제 조건 (Prerequisites)

- Java 21 이상
- Docker (데이터베이스 실행 시 권장)

### 설치 및 실행 (Installation & Running)

1. **저장소 클론**

   ```bash
   git clone <repository-url>
   cd findit-server
   ```

2. **환경 변수 설정**
   `.env.example` 파일을 복사하여 `.env` 파일을 생성하고 필요한 설정을 입력합니다.

   ```bash
   cp .env.example .env
   ```

   > **참고**: `.env` 파일에서 데이터베이스 연결 정보 및 API 키 등을 설정할 수 있습니다.

3. **데이터베이스 실행 (Docker 사용 시)**
   PostgreSQL 데이터베이스가 필요합니다. Docker를 사용하여 간단히 실행할 수 있습니다.

   ```bash
   docker run --name findit-db -e POSTGRES_PASSWORD=changeme -e POSTGRES_DB=findit -p 5432:5432 -d postgres:15-alpine
   ```

4. **애플리케이션 실행**

   ```bash
   ./gradlew bootRun
   ```

5. **테스트 실행**
   ```bash
   ./gradlew test
   ```

## 📚 API 문서 (API Documentation)

서버가 실행 중일 때 다음 주소에서 API 문서를 확인할 수 있습니다:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Health Check**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

## 🐳 배포 (Deployment)

이 프로젝트는 Docker 및 AWS EC2 배포를 지원합니다. 자세한 배포 가이드는 [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md) 문서를 참고하세요.

## 🤝 기여 (Contribution)

이 프로젝트에 기여하고 싶으신가요? 이슈를 등록하거나 Pull Request를 보내주세요.

## 📄 라이선스 (License)

[MIT License](LICENSE)
