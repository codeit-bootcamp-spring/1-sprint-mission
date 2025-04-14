# 베이스 이미지: Amazon Corretto 17
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 캐시 최적화를 위해 의존성 먼저 복사
COPY gradlew .
COPY gradle/ gradle/

# 프로젝트 소스 복사 (불필요한 파일은 .dockerignore에서 제외!)
COPY . .

# 환경 변수 설정: 프로젝트 정보
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

# Gradle Wrapper로 빌드
RUN ./gradlew build --no-daemon

# 포트 80 노출
EXPOSE 80

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
