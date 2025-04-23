# Amazon Corretto 17을 베이스 이미지로 사용
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 환경 변수 설정
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# 프로젝트 파일 복사
COPY . .

# Gradle Wrapper를 사용하여 애플리케이션 빌드
RUN ./gradlew clean build -x test

# 80 포트 노출
EXPOSE 80

# 애플리케이션 실행 명령어 설정
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar build/libs/$PROJECT_NAME-$PROJECT_VERSION.jar"]