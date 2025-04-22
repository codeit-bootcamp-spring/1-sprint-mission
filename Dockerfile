# Gradle로 빌드
FROM gradle:8.5-jdk17-alpine AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN ./gradlew build -x test --no-daemon

# 실행용
FROM amazoncorretto:17
WORKDIR /app

# [환경 변수 설정]
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

# [JAR 복사]
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# [포트 노출]
EXPOSE 80

# [실행 명령어]
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]