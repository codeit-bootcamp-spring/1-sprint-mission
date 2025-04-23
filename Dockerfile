# Build Stage
FROM amazoncorretto:17 AS builder
WORKDIR /app

#먼저 gradle 파일만 복사하여 의존성 레이어 캐싱
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
# 의존성 다운로드 (소스코드 변경과 무관하게 캐시 활용)
RUN ./gradlew dependencies --no-daemon

#소스 코드 복사 및 빌드
COPY src ./src
RUN ./gradlew build -x test --no-daemon

# Runtime Stage: 실행에 필요한 최소 구성만 포함
FROM amazoncorretto:17-alpine AS runtime
WORKDIR /app

# 환경 변수 설정
ENV PROJECT_NAME=discodeit \
PROJECT_VERSION=1.2-M8 \
JVM_OPTS=""

# 빌드 스테이지에서 생성된 JAR 파일만 복사
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ./app.jar

EXPOSE 80

CMD java $JVM_OPTS -jar app.jar