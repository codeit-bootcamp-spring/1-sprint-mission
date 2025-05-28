#!/bin/bash
echo "[1] Spring Boot 애플리케이션 빌드 중..."
./gradlew clean bootJar

echo "[2] JAR 파일을 Docker 컨텍스트로 복사 중..."
./gradlew copyJarToDockerContext

echo "[3] Docker 이미지 빌드 중..."
docker build -t hello-world-spring:1.0 .

echo "[4] Docker 컨테이너 실행 중..."
docker run -d -p 80:8080 --name hello-spring hello-world-spring:1.0

echo "[5] 컨테이너가 시작되었습니다! 다음 주소에서 애플리케이션에 접속하세요: http://localhost:8080"
echo "로그 확인하기: docker logs hello-spring"
echo "컨테이너 중지하기: docker stop hello-spring"fml;lalflfksffallrafkffjjwkk