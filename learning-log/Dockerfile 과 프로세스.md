https://github.com/codeit-bootcamp-spring/1-sprint-mission/pull/398

# exec form 을 이용하라

        (-) CMD ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
        (+) CMD ["sh", "-c", "exec", "java $JVM_OPTS -jar app.jar"]

이건홍 멘토님 "자바 어플리케이션이 1번 프로세스가 되어야 합니다."에 대한 말씀과 참조해주신 링크에 대한 간단 정리

## java 애플리케이션의 PID 1 프로세스로 배정하라

### 프로세스란?

Linux/Unix 시스템에서 어떤 프로그램을 실행하면, 프로세스에서 실행된다.
그리고 어떤 프로세스(A)가 다른 프로세스(B)를 실행하면, 그건 자식 프로세스(child process)(B)가 된다.

### Docker 명령어 실행과 프로세스 배정 예시

e.g. shell(A), java(B)

        ENTRYPOINT ["sh", "-c", "java -jar app.jar"]


        PID 1: sh (A)
        └── PID 2: java (B)

e.g. java(B)

        ENTRYPOINT ["sh", "-c", "exec java -jar app.jar"]

        PID 1: java

이때 Docker 는 SIGTERM, SIGINT 같은 시그널을 PID 1에게만 직접 보낸다. PID 1이 sh일 때, SIGTERM 시그널을 받았다면, 또한 시그널 전달 코드가
작성되어 있지 않다면 PID 2에서 돌아가는 java 는 신호를 받지 못해 좀비 프로세스가 된다.

## shell form vs exec from

shell form은 PID 8, exec form 은 PID 1 인 프로세스에서 스크립트를 실행시킨다. 이떄, PID 1 은 메인 프로세스이다.
컨테이너를 중지시킬 때, 메인 프로세스에 자식이 딸려있으면 자식 프로세스 먼저, 그 후에 메인 프로세스를 중지한다.
shell from은 PID 8 프로세스에서 스크립트를 실행시키므로 결국 컨테이너를 중지할 때 프로세스를 중지시키기 위해 두 개의 신호를 보내야 한다는 점에서 exec form 을
써야한다고 말하고 있는 것이다.


----
[참고 자료]

https://brunch.co.kr/@growthminder/142

https://engineering.pipefy.com/2021/07/30/1-docker-bits-shell-vs-exec/