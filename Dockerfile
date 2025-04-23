# ==========================
# Stage 1: Build
# ==========================
FROM gradle:8.12.1-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 캐시 최적화를 위해 의존성 먼저 복사
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# 의존성만 미리 받아두기
RUN ./gradlew dependencies --no-daemon

# 프로젝트 소스 복사 (불필요한 파일은 .dockerignore에서 제외!)
COPY . .

# 실제 빌드 수행
RUN ./gradlew clean build -x test --no-daemon

# ==========================
# Stage 2: Runtime
# ==========================
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 환경 변수 설정: 프로젝트 정보
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

# 빌드 결과물만 복사
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 포트 80 노출
EXPOSE 80

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
