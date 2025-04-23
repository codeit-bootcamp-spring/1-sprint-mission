FROM gradle:7.6-jdk17-alpine AS builder
WORKDIR /app

# 의존성 캐시
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x ./gradlew

# 의존성 다운로드
RUN ./gradlew dependencies --no-daemon

# 실제 코드 빌드
COPY src ./src
RUN ./gradlew -x test build --no-daemon


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

EXPOSE 80

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    SPRING_PROFILES_ACTIVE=prod \
    JVM_OPTS=""

ENV AWS_S3_BUCKET="" \
    AWS_ACCESS_KEY_ID="" \
    AWS_SECRET_ACCESS_KEY="" \
    AWS_REGION=""

# 빌더 스테이지에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar /app/discodeit.jar

# 환경 변수 설정 및 애플리케이션 실행
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'export SPRING_DATASOURCE_URL="jdbc:postgresql://$RDS_ENDPOINT/discodeit"' >> /app/start.sh && \
    echo 'COMMAND="java ${JVM_OPTS} -jar /app/discodeit.jar \
    --spring.profiles.active=${SPRING_PROFILES_ACTIVE} \
    --discodeit.storage.type=${STORAGE_TYPE} \
    --discodeit.storage.s3.access-key=${AWS_ACCESS_KEY_ID} \
    --discodeit.storage.s3.secret-key=${AWS_SECRET_ACCESS_KEY} \
    --discodeit.storage.s3.region=${AWS_REGION} \
    --discodeit.storage.s3.bucket=${AWS_S3_BUCKET} \
    --discodeit.storage.s3.presigned-url-expiration=${AWS_S3_PRESIGNED_URL_EXPIRATION} \
    --spring.datasource.url=${SPRING_DATASOURCE_URL} \
    --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
    --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}"' >> /app/start.sh && \
    echo 'echo "Executing: $COMMAND"' >> /app/start.sh && \
    echo 'exec $COMMAND' >> /app/start.sh && \
    chmod +x /app/start.sh

# 실행
ENTRYPOINT ["/app/start.sh"]