FROM amazoncorretto:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 환경변수 설정
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

# 전체 프로젝트 복사 (.dockerignore로 불필요 파일 제외)
COPY . /app

# Gradle Wrapper 사용하여 빌드 (테스트 제외)
RUN ./gradlew build -x test

# 80번 포트 노출
EXPOSE 80

# jar 파일 실행 (환경변수 활용)
CMD ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
