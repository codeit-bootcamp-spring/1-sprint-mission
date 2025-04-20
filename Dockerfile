
# 빌드 스테이지
FROM amazoncorretto:17 AS build
# 로컬의 모든 파일들을 이미지의 app경로 안으로 복사
COPY . /app
# 작업 디렉토리 설정
WORKDIR /app

RUN ls -al
#소스코드 빌드 (테스트 제외)
RUN ./gradlew build -x test



# 실행 스테이지
# 이미지 선택. 해당 파일 안엔 자바 런타임이 포함되어 있음.
FROM amazoncorretto:17 
# 작업 디렉토리 설정
WORKDIR /app
# 환경변수 정의
ENV PROJECT_NAME="discodeit"
ENV PROJECT_VERSION="1.2-M8"
# 데이터베이스 연결 설정
ARG SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/discodeit"
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ARG SPRING_DATASOURCE_USERNAME="discodeit_user"
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ARG SPRING_BOOT_ADMIN_CLIENT_URL="http://localhost:9090"
ENV SPRING_BOOT_ADMIN_CLIENT_URL=${SPRING_BOOT_ADMIN_CLIENT_URL}
ARG SPRING_PROFILES_ACTIVE="prod"
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
# 빌드 스테이지에서 빌드된 파일을 복사
COPY --from=build /app/build/libs/*.jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar
# JVM 실행 시에 전달할 설정값들을 담을 환경 변수
ARG JVM_OPTS=""
ENV JVM_OPTS=$JVM_OPTS

#외부 접근 포트 명시
EXPOSE 80
#접근권한 부여 
#RUN chmod +x $PROJECT_NAME-$PROJECT_VERSION.jar
RUN ls -al
# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar"]