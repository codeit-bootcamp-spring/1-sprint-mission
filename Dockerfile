#FROM amazoncorretto:17
FROM gradle:8.6-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
RUN gradle dependencies || true

COPY . .
#RUN ./gradlew build -x test --no-daemon
RUN gradle bootJar -x test --no-daemon

FROM amazoncorretto:17.0.8-alpine3.18 AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

#ENV PROJECT_NAME=discodeit
#ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]

EXPOSE 80

#CMD ["sh", "-c", "java $JVM_OPTS -jar build/libs/$PROJECT_NAME-$PROJECT_VERSION.jar"]
