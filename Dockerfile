# [1] Build Stage
FROM gradle:8.5.0-jdk17-alpine AS build
WORKDIR /app
# 변경 적은 것 먼저 복사해서 캐시 활용
COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle ./
COPY build.gradle ./
RUN chmod +x gradlew

# 변경 자주 되는 것 아래로 ↓
COPY . .

# 빌드 실행 (테스트 생략)
RUN ./gradlew build -x test --no-daemon


# [2] Frontend Extractor Stage
FROM amazoncorretto:17 AS frontend-extractor

WORKDIR /app

# bulid 스테이지에서 생성된 JAR 파일을 현제 스테이지로 복사
COPY --from=build /app/build/libs/*.jar app.jar

# JAR 파일을 압축 해제, BOOT~.. -> JAR 내부의 특정 경로만 추출
# 현재 디렉토리에 BOOT-INF/classes/static 폴더 생성
RUN jar -xf app.jar BOOT-INF/classes/static

# 추출된 정적 파일 폴더를 /frontend_build로 이동
RUN mv BOOT-INF/classes/static /frontend_build


# [3] Runtime Stage
FROM amazoncorretto:17 AS runtime
WORKDIR /app

# 빌드 결과물만 복사 (경로 주의!)
COPY --from=build /app/build/libs/*.jar app.jar
# 프론트엔드 정적 파일도 복사
COPY --from=frontend-extractor /frontend_build /frontend_build

# 포트 노출 및 환경변수
EXPOSE 8080
ENV JVM_OPTS=""

# 앱 실행
CMD ["sh", "-c", "exec java $JVM_OPTS -jar app.jar"]

