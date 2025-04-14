# [1] Build Stage
FROM gradle:8.5.0-jdk17-alpine AS build
WORKDIR /app
# 변경 적은 것 먼저 복사해서 캐시 활용
COPY build.gradle ./
COPY settings.gradle ./
RUN gradle dependencies --no-daemon || true

# 변경 자주 되는 것 아래로 ↓
COPY . .

# 빌드 실행 (테스트 생략)
RUN ./gradlew build -PskipTests --no-daemon

# [2] Runtime Stage
FROM amazoncorretto:17 AS runtime
WORKDIR /app

# 빌드 결과물만 복사 (경로 주의!)
COPY --from=build /app/build/libs/discodeit-1.2-M8.jar app.jar

# 포트 노출 및 환경변수
EXPOSE 80
ENV JVM_OPTS=""

# 앱 실행
CMD ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
