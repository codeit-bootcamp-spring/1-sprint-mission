프로젝트 마일스톤

- Java 프로젝트를 Spring 프로젝트로 마이그레이션
- 의존성 관리를 IoC Container에 위임하도록 리팩토링
- 비즈니스 로직 고도화
  기본 요구사항
  Spring 프로젝트 초기화
 - [x] Spring Initializr를 통해 zip 파일을 다운로드하세요.
   - [x] 빌드 시스템은 Gradle - Groovy를 사용합니다.
   - [x] 언어는 Java 17를 사용합니다.
   - [x] Spring Boot의 버전은 3.4.0입니다.
   - [x] GroupId는 com.sprint.mission입니다.
   - [x] ArtifactId와 Name은 discodeit입니다.
   - [x] packaging 형식은 Jar입니다
   - [x] Dependency를 추가합니다.
   - [x] Lombok
   - [x] Spring Web
 - [x] zip 파일을 압축해제하고 원래 진행 중이던 프로젝트에 붙여넣기하세요. 일부 파일은 덮어쓰기할 수 있습니다.
   - [x] application.properties 파일을 yaml 형식으로 변경하세요.
   - [x] DiscodeitApplication의 main 메서드를 실행하고 로그를 확인해보세요.


---

Bean 선언 및 테스트
- [x] File*Repository 구현체를 Repository 인터페이스의 Bean으로 등록하세요.
- [x] Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록하세요.
- [x] JavaApplication에서 테스트했던 코드를 DiscodeitApplication에서 테스트해보세요.
  - [x]  JavaApplication 의 main 메소드를 제외한 모든 메소드를 DiscodeitApplication클래스로 복사하세요.
  - [x]  JavaApplication의 main 메소드에서 Service를 초기화하는 코드를 Spring Context를 활용하여 대체하세요.

```java
// JavaApplication
public static void main(String[] args) {
    // 레포지토리 초기화
    // ...
    // 서비스 초기화
      UserService userService = new BasicUserService(userRepository);
    ChannelService channelService = new BasicChannelService(channelRepository);
    MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);

    // ...
}

// DiscodeitApplication
public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
    // 서비스 초기화
        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
      UserService userService;
    ChannelService channelService;
    MessageService messageService;

    // ...
}
```

   - [x]  JavaApplication의 main 메소드의 셋업, 테스트 부분의 코드를 DiscodeitApplication클래스로 복사하세요.
```java
public static void main(String[] args) {
    // ...
    // 셋업
    User user = setupUser(userService);
    Channel channel = setupChannel(channelService);
    // 테스트
    messageCreateTest(messageService, channel, user);
}
```

Spring 핵심 개념 이해하기
- [ ] JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.   
IoC Container   
Dependency Injection   
Bean   


Lombok 적용
- [x] 도메인 모델의 getter 메소드를 @Getter로 대체해보세요.
- [x] Basic*Service의 생성자를 @RequiredArgsConstructor로 대체해보세요.


비즈니스 로직 고도화
- [ ]  다음의 기능 요구 사항을 구현하세요.

추가 기능 요구사항
시간 타입 변경하기
- [ ] 시간을 다루는 필드의 타입은 Instant로 통일합니다.
  - 기존에 사용하던 Long보다 가독성이 뛰어나며, 시간대(Time Zone) 변환과 정밀한 시간 연산이 가능해 확장성이 높습니다.  

**새로운 도메인 추가하기**
- [ ] 공통: 앞서 정의한 도메인 모델과 동일하게 공통 필드(id, createdAt, updatedAt)를 포함합니다.
- [ ] ReadStatus
  - 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
- [ ] UserStatus
  - 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
  - [ ] 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요. 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
- [ ] BinaryContent
  - 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
  - [ ] 수정 불가능한 도메인 모델로 간주합니다. 따라서 updatedAt 필드는 정의하지 않습니다.
  - [ ] User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 id 참조 필드를 추가하세요.
- [ ] 각 도메인 모델 별 레포지토리 인터페이스를 선언하세요.
레포지토리 구현체(File, JCF)는 아직 구현하지 마세요. 이어지는 서비스 고도화 요구사항에 따라 레포지토리 인터페이스에 메소드가 추가될 수 있어요.