# 빌드 스테이지: 애플리케이션 빌드를 위한 환경
FROM amazoncorretto:17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# 종속성 캐싱을 위해 Gradle 파일만 먼저 복사
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Gradle Wrapper 실행 권한 설정
RUN chmod +x ./gradlew

# 종속성 다운로드 - 이 단계는 소스코드가 변경되어도 캐시됨
RUN ./gradlew dependencies --no-daemon

# 소스코드 복사
COPY src /app/src

# 프로젝트 정보 설정
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8

# 애플리케이션 빌드
RUN ./gradlew build -x test -x checkstyleMain -x checkstyleTest --no-daemon

# 런타임 스테이지: 최소한의 실행 환경
FROM amazoncorretto:17-alpine AS runtime

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 정보와 JVM 옵션 설정
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

# 빌드 스테이지에서 생성된 JAR 파일만 복사
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar /app/application.jar

RUN mkdir -p /app/data/binary-content

# 80 포트 노출
EXPOSE 80

# 애플리케이션 실행 명령어 설정
ENTRYPOINT java ${JVM_OPTS} -jar /app/application.jar

## Amazon Corretto 17 이미지를 베이스 이미지로 사용
#FROM amazoncorretto:17
#
## 작업 디렉토리 설정
#WORKDIR /app
#
## 프로젝트 정보를 환경 변수로 설정
#ENV PROJECT_NAME=discodeit
#ENV PROJECT_VERSION=1.2-M8
#ENV JVM_OPTS=""
#
## 프로젝트 파일을 컨테이너로 복사 (불필요한 파일은 .dockerignore로 제외)
#COPY . /app
#
## Gradle Wrapper를 사용하여 애플리케이션을 빌드
#RUN chmod +x ./gradlew
#RUN ./gradlew build -x test -x checkstyleMain -x checkstyleTest
#
## 빌드된 JAR 파일 확인 (디버깅용)
#RUN ls -la /app/build/libs/
#
## 80 포트 노출
#EXPOSE 80
#
## 애플리케이션 실행 명령어 설정 (환경변수 활용)
#ENTRYPOINT java ${JVM_OPTS} -jar /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar