FROM amazoncorretto:17 AS builder

WORKDIR /app

COPY . /app

RUN ./gradlew build --no-daemon

FROM amazoncorretto:17

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

