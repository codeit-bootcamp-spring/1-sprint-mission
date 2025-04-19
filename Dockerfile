FROM amazoncorretto:17

WORKDIR /app

COPY ./ ./

RUN ./gradlew build #-x test  # 나중에 바꾸기

EXPOSE 80

ENV SPRING_PROFILES_ACTIVE=prod
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

ENTRYPOINT ["sh", "-c", "exec java ${JVM_OPTS} -jar /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
