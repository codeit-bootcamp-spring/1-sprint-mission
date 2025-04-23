FROM gradle:7.6.0-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle


RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

COPY . .

RUN chmod +x gradlew && ./gradlew build --no-daemon

FROM eclipse-temurin:17-jre-jammy

ARG PROJECT_NAME
ARG PROJECT_VERSION
ARG JVM_OPTS

ENV PROJECT_NAME=${PROJECT_NAME:-discodeit}
ENV PROJECT_VERSION=${PROJECT_VERSION:-1.2-M8}
ENV JVM_OPTS=${JVM_OPTS:-}

WORKDIR /app

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT java $JVM_OPTS -jar app.jar --spring.profiles.active=prod

