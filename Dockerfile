# 1단계: 빌드 스테이지
FROM amazoncorretto:17 as builder

# 작업 디렉터리 설정
WORKDIR /app

# Gradle이나 Maven을 사용하는 경우, build.gradle / pom.xml 및 소스 코드 복사
COPY . /app

# 애플리케이션 빌드 (여기서는 Gradle 기준)
RUN ./gradlew clean build -x test

# 2단계: 실행 스테이지
FROM amazoncorretto:17

# 작업 디렉터리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사 (위 스테이지에서 복사)
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 80
# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
