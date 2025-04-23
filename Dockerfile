#Builder Stage
FROM gradle:7.6.0-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x ./gradlew

RUN ./gradlew dependencies --no-daemon

COPY src ./src
RUN ./gradlew -x test build --no-daemon

#Runtime Stage
FROM amazoncorretto:17-alpine
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar


EXPOSE 80

ENTRYPOINT ["sh", "-c"]
CMD ["java $JVM_OPTS -jar app.jar"]