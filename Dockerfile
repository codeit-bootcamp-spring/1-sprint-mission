# Builder Stage
FROM gradle:7.6.0-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app
COPY . /app

# Gradle Wrapper 사용하여 빌드
RUN ./gradlew build --no-daemon

# Runtime Stage 베이스 이미지 지정
FROM amazoncorretto:17

# 80 포트 노출
EXPOSE 80

# 환경 변수 설정 (프로젝트 이름, 버전, JVM 옵션: 기본값은 빈 문자열)
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS="-Dspring.profiles.active=prod -Dserver.port=80"

# Builder Stage에서 빌드된 JAR 파일 복사 (가져오기) /app/app.jar는 경로 이름을 이걸로 바꾼다는 것.
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar /app/app.jar

# 애플리케이션 실행 명령어 설정 (위에서 build/libs/~.jar 경로를 app/app.jar로 바꿨으니까)
CMD ["java", "-Dspring.profiles.active=prod", "-Dserver.port=80", "-jar", "/app/app.jar"]
