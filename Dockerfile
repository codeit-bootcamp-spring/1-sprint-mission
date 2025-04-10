# [1] 베이스 이미지 지정 : amazoncorretto:17
FROM amazoncorretto:17

# [2] 작업 디렉토리 설정
WORKDIR /app

# [3] 컨테이너로 프로젝트 파일 복사 & 해당 작업시 .dockerignore 이 추가적으로 관여한다.
COPY . .

# [4] Gradle Wrapper를 사용하여 애플리케이션을 빌드
RUN chmod +x gradlew && ./gradlew build -PskipTests --no-daemon

# [5] 80 포트 노출
EXPOSE 80

# [6] 환경변수 관리
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# [7] 애플리케이션 실행
CMD java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar