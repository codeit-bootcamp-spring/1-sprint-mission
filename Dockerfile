FROM gradle:8.12.1-jdk17 AS builder
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon --stacktrace -x test

FROM amazoncorretto:17
WORKDIR /app
EXPOSE 80
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=2.3-M11
ENV JVM_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]