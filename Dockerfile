FROM amazoncorretto:17

WORKDIR /app

COPY ./ ./

RUN ./gradlew build -x test

EXPOSE 80

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

ENTRYPOINT ["sh", "-c", "exec java ${JVM_OPTS} -jar /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
