# 진행 기록 (FindIt Server)

## 2025-10-05

- 데이터베이스 드라이버를 MySQL → PostgreSQL로 전환 (`build.gradle`, `application.properties`, `application-example.properties`).
- 경찰청 API 연동을 비활성화 할 수 있는 feature flag 추가 (`police.api.enabled`), 비활성 시 내부 데이터만 사용하도록 서비스/스케줄러/헬스 인디케이터 수정.
- `.env` 자동 로딩 구성(`spring.config.import=optional:file:.env[.properties]`), Docker 배포 전용 프로필 `application-docker.properties` 추가.
- `.env.example`과 `docs/DEPLOYMENT.md`로 AWS EC2 + Docker 배포 절차 및 Secrets Manager/Parameter Store 활용 가이드 작성.
- 경찰청 API 비활성 플래그 동작을 검증하는 단위 테스트 `PoliceApiClientTest` 추가.
- 권한 이슈 때문에 Gradle 캐시 경로 재지정이 필요함을 확인 (`GRADLE_USER_HOME` 설정 권장).
