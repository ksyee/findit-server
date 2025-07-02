# ---------- Build Stage (multi-stage optional) ----------
# 프로젝트가 단순 Gradle 빌드 산출물(JAR)만 필요하므로 별도 build stage 생략

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre AS runtime

# JAR 파일 경로를 빌드 시 인자로 받아 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 외부에서 포트 매핑할 기본 포트
EXPOSE 8080

# 컨테이너 실행 시 JVM 옵션을 환경 변수로 주입할 수 있도록
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
