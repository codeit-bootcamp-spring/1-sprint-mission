# Build Stage
FROM amazoncorretto:17 AS builder

WORKDIR /app

# 의존 파일 우선적으로 복사 (캐시 유지)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 의존성 캐싱을 위한 우선 다운로드
RUN yum update -y && yum install -y bash curl unzip \
    && chmod +x ./gradlew \
    && ./gradlew dependencies --no-daemon || return 0

COPY . .

RUN chmod +x ./gradlew && ./gradlew clean bootJar --no-daemon

# Runtime Stage
FROM amazoncorretto:17-alpine

ARG PROJECT_NAME
ARG PROJECT_VERSION

LABEL project_name=${PROJECT_NAME}
LABEL project_version=${PROJECT_VERSION}

WORKDIR /app

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 80

RUN apk add --no-cache curl

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
CMD curl --quiet --tries=1 --spider http://localhost:80/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JVM_OPTS -jar app.jar"]
