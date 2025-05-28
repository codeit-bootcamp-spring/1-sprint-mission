FROM gradle:8.12.1-jdk17 AS builder
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon --stacktrace

FROM amazoncorretto:17
WORKDIR /app
EXPOSE 80
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]