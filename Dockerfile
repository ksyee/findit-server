# ---------- Build Stage ----------
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Gradle wrapper와 빌드 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 의존성 다운로드 (캐싱 최적화)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드 (테스트 제외)
RUN ./gradlew clean bootJar -x test --no-daemon

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

# 빌드 단계에서 생성된 JAR 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 비root 사용자로 실행 (보안)
RUN useradd -m appuser
USER appuser

# 포트 노출
EXPOSE 8080

# 헬스체크
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM 옵션 환경 변수
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
