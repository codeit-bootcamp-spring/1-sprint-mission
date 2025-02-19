# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리

## **기본 요구사항**

### **Spring 프로젝트 초기화**

- [x]  [ ] **Spring Initializr**를 통해 zip 파일을 다운로드하세요.
    - [x]  빌드 시스템은 Gradle - Groovy를 사용합니다.
    - [x]  언어는 Java 17를 사용합니다.
    - [x]  Spring Boot의 버전은 `3.4.0`입니다.
    - [x]  GroupId는 `com.sprint.mission`입니다.
    - [x]  ArtifactId와 Name은 `discodeit`입니다.
    - [x]  packaging 형식은 `Jar`입니다
    - [x]  Dependency를 추가합니다.
        - [x]  Lombok
        - [x]  Spring Web
- [x]  zip 파일을 압축해제하고 원래 진행 중이던 프로젝트에 붙여넣기하세요. 일부 파일은 덮어쓰기할 수 있습니다.
- [x]  [ ] `application.properties` 파일을 `yaml` 형식으로 변경하세요.
- [x]  [ ] `DiscodeitApplication`의 main 메서드를 실행하고 로그를 확인해보세요.

### **Bean 선언 및 테스트**

- [x]  File*Repository 구현체를 Repository 인터페이스의 Bean으로 등록하세요.
- [x]  Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록하세요.
- [x]  [ ] `JavaApplication`에서 테스트했던 코드를 `DiscodeitApplication`에서 테스트해보세요.
    - [x]  `JavaApplication` 의 main 메소드를 제외한 모든 메소드를 `DiscodeitApplication`클래스로 복사하세요.
    - [x]  `JavaApplication`의 main 메소드에서 Service를 초기화하는 코드를 Spring Context를 활용하여 대체하세요.
        
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
        
    - [x]  `JavaApplication`의 main 메소드의 셋업, 테스트 부분의 코드를 `DiscodeitApplication`클래스로 복사하세요.
        
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
        

### **Spring 핵심 개념 이해하기**

- [x]  [ ] `JavaApplication`과 `DiscodeitApplication`에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.
    - IoC Container
    - Dependency Injection
    - Bean

### **Lombok 적용**

- [x]  도메인 모델의 getter 메소드를 `@Getter`로 대체해보세요.
- [x]  [ ] `Basic*Service`의 생성자를 `@RequiredArgsConstructor`로 대체해보세요.

### **비즈니스 로직 고도화**

## **추가 기능 요구사항**

### **시간 타입 변경하기**

- [x]  시간을 다루는 필드의 타입은 `Instant`로 통일합니다.
    - 기존에 사용하던 Long보다 가독성이 뛰어나며, 시간대(Time Zone) 변환과 정밀한 시간 연산이 가능해 확장성이 높습니다.

### **새로운 도메인 추가하기**

- [x]  공통: 앞서 정의한 도메인 모델과 동일하게 공통 필드(`id`, `createdAt`, `updatedAt`)를 포함합니다.
- [x]  [ ] `ReadStatus`
    - 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
- [x]  [ ] `UserStatus`
    - 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
    - [x]  마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요.
        - 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
- [x]  [ ] `BinaryContent`
    - 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
    - [x]  수정 불가능한 도메인 모델로 간주합니다. 따라서 `updatedAt` 필드는 정의하지 않습니다.
    - [ ]  [ ] `User`, `Message` 도메인 모델과의 의존 관계 방향성을 잘 고려하여 `id` 참조 필드를 추가하세요.
- [x]  각 도메인 모델 별 레포지토리 인터페이스를 선언하세요.
    - 레포지토리 구현체(File, JCF)는 아직 구현하지 마세요. 이어지는 서비스 고도화 요구사항에 따라 레포지토리 인터페이스에 메소드가 추가될 수 있어요.

### **DTO 활용하기**

[DTO란?](https://www.codeit.kr/topics/6789bcf706985c5b264f9ee9/lessons/679c2fe348fe9b1f66b1df32)

### **UserService 고도화**

- **고도화**
    - `create`
        - [x]  선택적으로 프로필 이미지를 같이 등록할 수 있습니다.
        - [x]  DTO를 활용해 파라미터를 그룹화합니다.
            - 유저를 등록하기 위해 필요한 파라미터, 프로필 이미지를 등록하기 위해 필요한 파라미터 등
        - [x]  [ ] `username`과 `email`은 다른 유저와 같으면 안됩니다.
        - [x]  [ ] `UserStatus`를 같이 생성합니다.
    - `find`, `findAll`
        - DTO를 활용하여:
            - [x]  사용자의 온라인 상태 정보를 같이 포함하세요.
            - [x]  패스워드 정보는 제외하세요.
    - `update`
        - [x]  선택적으로 프로필 이미지를 대체할 수 있습니다.
        - [x]  DTO를 활용해 파라미터를 그룹화합니다.
            - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
    - `delete`
        - [x]  관련된 도메인도 같이 삭제합니다.
            - `BinaryContent`(프로필), `UserStatus`

## **AuthService 구현**

- `login`
    - [x]  [ ] `username`, `password`과 일치하는 유저가 있는지 확인합니다.
        - [x]  일치하는 유저가 있는 경우: 유저 정보 반환
        - [x]  일치하는 유저가 없는 경우: 예외 발생
    - [x]  DTO를 활용해 파라미터를 그룹화합니다.

### **ChannelService 고도화**

- **고도화**
    - `create`
        - **PRIVATE** 채널과 **PUBLIC** 채널을 생성하는 메소드를 분리합니다.
        - [ ]  분리된 각각의 메소드를 DTO를 활용해 파라미터를 그룹화합니다.
        - PRIVATE 채널을 생성할 때:
            - [ ]  채널에 참여하는 `User`의 정보를 받아 `User` 별 `ReadStatus` 정보를 생성합니다.
            - [ ]  [ ] `name`과 `description` 속성은 생략합니다.
        - **PUBLIC** 채널을 생성할 때에는 기존 로직을 유지합니다.
    - `find`
        - DTO를 활용하여:
            - [ ]  해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
            - [ ]  [ ] **PRIVATE** 채널인 경우 참여한 `User`의 `id` 정보를 포함합니다.
    - `findAll`
        - DTO를 활용하여:
            - [ ]  해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
            - [ ]  [ ] **PRIVATE** 채널인 경우 참여한 `User`의 `id` 정보를 포함합니다.
        - [ ]  특정 `User`가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. `findAllByUserId`
        - [ ]  [ ] **PUBLIC** 채널 목록은 전체 조회합니다.
        - [ ]  [ ] **PRIVATE** 채널은 조회한 `User`가 참여한 채널만 조회합니다.
    - `update`
        - [ ]  DTO를 활용해 파라미터를 그룹화합니다.
            - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
        - [ ]  PRIVATE 채널은 수정할 수 없습니다.
    - `delete`
        - [ ]  관련된 도메인도 같이 삭제합니다.
            - `Message`, `ReadStatus`

### **MessageService 고도화**

- **고도화**
    - `create`
        - [ ]  선택적으로 여러 개의 첨부파일을 같이 등록할 수 있습니다.
        - [ ]  DTO를 활용해 파라미터를 그룹화합니다.
    - `findAll`
        - [ ]  특정 `Channel`의 Message 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. `findallByChannelId`
    - `update`
        - [ ]  DTO를 활용해 파라미터를 그룹화합니다.
            - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
    - `delete`
        - [ ]  관련된 도메인도 같이 삭제합니다.
            - 첨부파일(`BinaryContent`)

### **ReadStatusService 구현**

- `create`
    - [x]  DTO를 활용해 파라미터를 그룹화합니다.
    - [x]  관련된 `Channel`이나 `User`가 존재하지 않으면 예외를 발생시킵니다.
    - [x]  같은 `Channel`과 `User`와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
- `find`
    - [x]  [ ] `id`로 조회합니다.
- `findAllByUserId`
    - [x]  [ ] `userId`를 조건으로 조회합니다.
- `update`
    - [x]  DTO를 활용해 파라미터를 그룹화합니다.
        - 수정 대상 객체의 `id` 파라미터, 수정할 값 파라미터
- `delete`
    - [x]  [ ] `id`로 삭제합니다.

### **UserStatusService 고도화**

- `create`
    - [x]  DTO를 활용해 파라미터를 그룹화합니다.
    - [x]  관련된 `User`가 존재하지 않으면 예외를 발생시킵니다.
    - [x]  같은 `User`와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
- `find`
    - [x]  [ ] `id`로 조회합니다.
- `findAll`
    - [x]  모든 객체를 조회합니다.
- `update`
    - [x]  DTO를 활용해 파라미터를 그룹화합니다.
        - 수정 대상 객체의 `id` 파라미터, 수정할 값 파라미터
- `updateByUserId`
    - [x]  [ ] `userId` 로 특정 `User`의 객체를 업데이트합니다.
- `delete`
    - [x]  [ ] `id`로 삭제합니다.

### **BinaryContentService 구현**

- `create`
    - [x]  DTO를 활용해 파라미터를 그룹화합니다.
- `find`
    - [x]  [ ] `id`로 조회합니다.
- `findAllByIdIn`
    - [x]  [ ] `id` 목록으로 조회합니다.
- `delete`
    - [x]  [ ] `id`로 삭제합니다.

### **새로운 도메인 Repository 구현체 구현**

- [ ]  지금까지 인터페이스로 설계한 각각의 Repository를 JCF, File로 각각 구현하세요.


### 멘토님께
1. DTO 설정 방식: 도메인별 vs 데이터 & 리퀘스트 분리
   DTO를 설정할 때, 도메인별로 나누는 것과 데이터 & 리퀘스트로 분리하는 것 중 어떤 방식이 더 적절한지 궁금합니다.
   모범답안에서는 데이터와 리퀘스트를 나눠서 관리하고 있던데, 이 방식이 더 좋은 이유가 무엇인지 궁금합니다!


2. 패키지 구조의 깊이 문제
   DTO를 도메인별로 나누려다 보니, 예를 들어 user 도메인에 request와 response를 별도의 패키지로 생성하면서
   `/com/sprint/mission/discodeit/dto/user/request/LoginRequest.java` 처럼 패키지 구조가 깊어지고 있는데, 이렇게 해도 괜찮을까요?
   혹시 패키지 구조가 너무 깊어지면 문제점이 있는지 궁금합니다.

3. DTO를 클래스로 만드는 것과 record로 만드는 것의 장단점..?
   어떤 것을 쓰는 것이 더 효과적일지 궁금합니다


4. 맨 처음 작성시에 리스트로 작성했다가 중간에 리스트가 비효율적인 것 같아서 맵으로 변경했습니다. 그런데 이렇게 되니 findAll이라는 전체 조회 때문에 리스트가 쓰이는데 이럴 경우 리스트랑 맵이 혼합으로 사용되고 있게 되는데 그럼 코드가 복잡해지는데 그래도 혼합해서 사용하는게 좋을까요? 일단은 제출을 위해서 다시 전부 리스트로 통합히랴고 다시 수정한 상황입니다.
   

   
