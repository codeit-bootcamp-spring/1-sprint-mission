FROM amazoncorretto:17 AS builder

WORKDIR /app
COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build --no-daemon -x test

FROM amazoncorretto:17

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

WORKDIR /app

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
