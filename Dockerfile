# 베이스 이미지 지정
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 현재 디렉토리의 파일(프로젝트)을 복사
COPY . .

# wait-for-it.sh 복사
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# Gradle Wrapper로 애플리케이션 빌드
# 테스트 제외하고 빌드
RUN ./gradlew build -x test

# 80 포트 노출
EXPOSE 80

# 프로젝트 정보를 환경 변수로 설정
ENV PROJECT_NAME="discodeit" PROJECT_VERSION="1.2-M8"

# JVM 옵션을 환경 변수로 설정
ENV JVM_OPTS=""

# 애플리케이션 실행 명령어 설정
# sh, -c : 리눅스 쉘 명령어에서 문자열 전체를 하나의 명령어처럼 실행하라
  # 환경변수를 문자열 안에서 해석해주기 위해 필요
CMD ["sh", "-c", "./wait-for-it.sh postgres:5432 -- java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
