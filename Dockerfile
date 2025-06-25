# =====================
# (1) Builder Stage
# 빌드 도구(Gradle)를 포함한 이미지를 사용하여 JAR 파일 생성
# =====================
FROM gradle:7.6.0-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 의존성 캐싱을 위해 Gradle 파일 먼저 복사 (의존성 캐시 최적화 패턴?)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Gradle 래퍼 스크립트 복사 및 권한 설정
COPY gradlew ./

## git 컨피그 설정 파일 생성 후, git add 캐시 지우고 다시 스테이징

# docker에서 window CRLF -> LF 변경
#RUN apt-get update \
#    && apt-get install -y dos2unix \
#    && dos2unix ./gradlew \
#    && chmod +x ./gradlew

# IDE에서 직접 변경 시 사용
RUN chmod +x ./gradlew

# 의존성 다운로드 - 소스 변경 시에도 이 단계는 캐시됨
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src ./src

# 빌드 실행
RUN ./gradlew bootJar --no-daemon


# ===========================
# (2) Runtime Stage
# 실제 실행에 필요한 최소한의 파일만 포함한 경량 이미지
# ===========================
FROM amazoncorretto:17.0.7-alpine

WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=v3.0-M12
ENV JVM_OPTS=""

COPY --from=builder /app/build/libs/*.jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar

EXPOSE 80

# 컨테이너 시작 시 실행할 명령어
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
