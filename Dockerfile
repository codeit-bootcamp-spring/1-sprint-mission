FROM amazoncorretto:17

WORKDIR /app

COPY . .

RUN ./gradlew build -x test --no-daemon

EXPOSE 80

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

CMD java $JVM_OPTS -jar build/libs/1-sprint-misson-1.2-M8.jar