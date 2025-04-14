# 베이스 이미지로 Amazon Corretto 17-alpine 사용
FROM amazoncorretto:17-alpine

# 환경 변수 설정
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=0.0.1-SNAPSHOT \
    JVM_OPTS="-Xmx512m -Xms256m"

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 파일과 소스 코드 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY docker-build.gradle .
COPY src src

# Gradle Wrapper에 실행 권한 부여
RUN chmod +x ./gradlew

# 애플리케이션 빌드 (테스트 스킵 및 도커용 build.gradle 사용)
RUN ./gradlew -b docker-build.gradle clean bootJar -x test --info

# 포트 노출
EXPOSE 80

# 애플리케이션 실행
CMD java $JVM_OPTS -jar build/libs/$PROJECT_NAME-$PROJECT_VERSION.jar 