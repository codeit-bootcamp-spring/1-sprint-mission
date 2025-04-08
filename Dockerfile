FROM openjdk:17-jdk

COPY build/libs/*0.0.1.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
