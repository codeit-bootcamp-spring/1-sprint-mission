# 1. Amazon Corretto 17 이미지를 베이스 이미지로 사용하세요.
FROM amazoncorretto:17

# 2. 작업 디렉토리를 설정하세요. (/app)
WORKDIR /app

# 3. 프로젝트 파일을 컨테이너로 복사하세요. (.dockerignore 참고)
COPY . .

# 4. Gradle Wrapper 실행 권한 부여
RUN chmod +x ./gradlew

# 5. Gradle Wrapper를 사용하여 애플리케이션을 빌드하세요. (테스트 제외)
RUN ./gradlew build -x test --no-daemon

# 6. 80 포트를 노출하도록 설정하세요.
EXPOSE 80

# 7. 프로젝트 정보를 환경 변수로 설정하세요.
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8

# JVM 옵션을 ENV로 설정
#ENV JVM_OPTS=$JVM_OPTS

# 빌드된 jar 파일 실행
#ENTRYPOINT sh -c "exec java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]

