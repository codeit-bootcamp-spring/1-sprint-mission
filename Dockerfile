FROM gradle:7.6.4-jdk17 AS builder

WORKDIR /app

COPY gradlew ./
COPY gradle/ gradle/
RUN chmod +x gradlew

COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies

COPY . .
RUN chmod +x gradlew

RUN ./gradlew clean build --no-daemon

FROM amazoncorretto:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app

EXPOSE 80

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

ENTRYPOINT ["/bin/bash", "-c", "java -jar ${JVM_OPTS} ${PROJECT_NAME}-${PROJECT_VERSION}.jar"]