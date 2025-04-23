# === Build Stage ===
FROM gradle:8.6-jdk17-alpine AS builder
WORKDIR /build-env

COPY gradlew ./gradlew
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
RUN apk add --no-cache dos2unix \
    && dos2unix ./gradlew \
    && chmod +x ./gradlew

RUN ./gradlew dependencies --no-daemon
COPY src ./src
RUN ./gradlew clean build -x test --no-daemon

# === Runtime Stage ===
FROM amazoncorretto:17-alpine
ARG PROJECT_NAME=discodeit
ARG PROJECT_VERSION=1.2-M8
ENV PROJECT_NAME=${PROJECT_NAME} \
    PROJECT_VERSION=${PROJECT_VERSION} \
    JVM_OPTS=""

WORKDIR /app
COPY --from=builder /build-env/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 외부에 80 포트로 노출하고, prod 프로필로 실행
EXPOSE 80
ENTRYPOINT ["sh","-c","java $JVM_OPTS -jar app.jar --spring.profiles.active=prod"]
