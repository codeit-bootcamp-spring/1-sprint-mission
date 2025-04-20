# ✅ [1] Amazon Corretto 17 베이스 이미지
FROM amazoncorretto:17

# ✅ [2] 작업 디렉토리 설정
WORKDIR /app

# ✅ [3] 프로젝트 파일 복사 (불필요한 건 .dockerignore로 필터링)
COPY . .

# ✅ [4] Gradle Wrapper를 사용하여 애플리케이션 빌드
RUN ./gradlew bootJar

# ✅ [5] 80 포트 노출
EXPOSE 8080

# ✅ [6] 환경 변수 설정 (프로젝트 정보 및 JVM 옵션)
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# ✅ [7] 실행 명령어 설정 (환경 변수 활용)
ENTRYPOINT ["java", "-Xmx384m", "-jar", "build/libs/discodeit-1.2-M8.jar"]