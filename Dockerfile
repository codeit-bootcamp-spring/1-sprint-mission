# 1. Amazon Corretto 17을 베이스 이미지로 사용
FROM amazoncorretto:17

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 불필요한 파일을 제외한 프로젝트 복사
COPY . .

# 4. Gradle Wrapper를 사용하여 빌드 (Gradle Wrapper 있는 경우만!)
RUN ./gradlew clean build -x test

# 5. 환경 변수 설정
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# 6. 컨테이너에서 사용할 포트 노출
EXPOSE 80

# 7. 실행 명령어 (환경변수를 이용해 jar 파일 실행)
CMD java $JVM_OPTS -jar ./build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar