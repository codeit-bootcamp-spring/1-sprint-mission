# 빌드 이미지
FROM gradle:8.10-jdk17-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 필요한 거 먼저 복사
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# 의존성 미리 다운(캐시)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 전체 프로젝트 복사 (.dockerignore로 불필요 파일 제외)
COPY . /app

# Gradle Wrapper 사용하여 빌드 (테스트 제외)
RUN chmod +x gradlew && ./gradlew build -x test

# 실행 이미지
FROM amazoncorretto:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 환경변수 설정
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV SPRING_PROFILE=prod
ENV JVM_OPTS=""

# 전체 프로젝트 복사 (.dockerignore로 불필요 파일 제외)
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar /app/app.jar

# 80번 포트 노출
EXPOSE 80

# jar 파일 실행 (환경변수 활용)
CMD ["sh", "-c", "java $JVM_OPTS -Dspring.profiles.active=$SPRING_PROFILE -jar /app/app.jar"]
