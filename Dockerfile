######## 1) 빌드 스테이지 ########
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew bootJar -x test --no-daemon   # build/libs/*.jar 생성

######## 2) 런타임 스테이지 ########
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# ★ 와일드카드로 “가장 최신” JAR 하나를 app.jar 로 복사
ARG JAR_FILE=/workspace/build/libs/*.jar
COPY --from=builder ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080