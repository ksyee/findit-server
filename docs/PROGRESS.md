# 진행 기록 (FindIt Server)

## 2025-10-05

- 데이터베이스 드라이버를 MySQL → PostgreSQL로 전환 (`build.gradle`, `application.properties`, `application-example.properties`).
- 경찰청 API 연동을 비활성화 할 수 있는 feature flag 추가 (`police.api.enabled`), 비활성 시 내부 데이터만 사용하도록 서비스/스케줄러/헬스 인디케이터 수정.
- `.env` 자동 로딩 구성(`spring.config.import=optional:file:.env[.properties]`), Docker 배포 전용 프로필 `application-docker.properties` 추가.
- `.env.example`과 `docs/DEPLOYMENT.md`로 AWS EC2 + Docker 배포 절차 및 Secrets Manager/Parameter Store 활용 가이드 작성.
- 경찰청 API 비활성 플래그 동작을 검증하는 단위 테스트 `PoliceApiClientTest` 추가.
- 권한 이슈 때문에 Gradle 캐시 경로 재지정이 필요함을 확인 (`GRADLE_USER_HOME` 설정 권장).
- API 키 기반 인증 도입: Spring Security 의존성 추가, `ApiKeyAuthenticationFilter`/`SecurityConfig`/`SecurityProperties` 구현, `.env`·프로퍼티·배포 문서 업데이트, 필터 단위 테스트 작성 예정.
- Flyway를 도입하여 DB 스키마/인덱스 마이그레이션을 `db/migration`으로 관리하고, JPA DDL-auto를 `validate`로 전환.
- PostgreSQL 환경에 맞춰 JDBC 배치 업서트 로직을 `ON CONFLICT (atc_id)`로 갱신하여 MySQL 전용 구문 제거.
- 테스트 실행 시 보안 설정이 걸리지 않도록 `src/test/resources/application.properties`에 테스트용 API 보안 설정 추가.
