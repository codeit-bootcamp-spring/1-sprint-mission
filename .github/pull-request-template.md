## 요구사항

### 기본 요구사항
### Spring 프로젝트 초기화
- [x] Spring Initializr를 통해 zip 파일을 다운로드하세요.
  - [X] 빌드 시스템은 Gradle - Groovy를 사용합니다.
  - [X] 언어는 Java 17를 사용합니다.
  - [X] Spring Boot의 버전은 3.4.0입니다.
  - [X] GroupId는 com.sprint.mission입니다.
  - [x] ArtifactId와 Name은 discodeit입니다.
  - [X] packaging 형식은 Jar입니다
  - [x] Dependency를 추가합니다.
    - [X] Lombok
    - [x] Spring Web
  - [X] zip 파일을 압축해제하고 원래 진행 중이던 프로젝트에 붙여넣기하세요. 일부 파일은 덮어쓰기할 수 있습니다.
  - [X] application.properties 파일을 yaml 형식으로 변경하세요.
  - [X] DiscodeitApplication의 main 메서드를 실행하고 로그를 확인해보세요.
![img.png](img.png)

### Bean 선언 및 테스트
- [X] File*Repository 구현체를 Repository 인터페이스의 Bean으로 등록하세요.
- [X] Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록하세요.
- [X] JavaApplication에서 테스트했던 코드를 DiscodeitApplication에서 테스트해보세요.
  - [x]  JavaApplication 의 main 메소드를 제외한 모든 메소드를 DiscodeitApplication클래스로 복사하세요.
  - [x]  JavaApplication의 main 메소드에서 Service를 초기화하는 코드를 Spring Context를 활용하여 대체하세요.
  - [x]  JavaApplication의 main 메소드의 셋업, 테스트 부분의 코드를 DiscodeitApplication클래스로 복사하세요.

### Spring 핵심 개념 이해하기
- [x] JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.
  - IoC Container
  - Dependency Injection
  - Bean
  
### Lombok 적용
  - [x] 도메인 모델의 getter 메소드를 @Getter로 대체해보세요.
  - [x] Basic*Service의 생성자를 @RequiredArgsConstructor로 대체해보세요.




### 추가 기능 요구사항
### 시간 타입 변경하기
- [X] 시간을 다루는 필드의 타입은 Instant로 통일합니다.
  - 기존에 사용하던 Long보다 가독성이 뛰어나며, 시간대(Time Zone) 변환과 정밀한 시간 연산이 가능해 확장성이 높습니다.

### 새로운 도메인 추가하기
- [x] 공통: 앞서 정의한 도메인 모델과 동일하게 공통 필드(id, createdAt, updatedAt)를 포함합니다.
- [x] ReadStatus
  - 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
- [X] UserStatus
  - 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
  - [x] 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요.
    - 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
- [X] BinaryContent
    - 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
    - [X] 수정 불가능한 도메인 모델로 간주합니다. 따라서 updatedAt 필드는 정의하지 않습니다.
    - [X] User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 id 참조 필드를 추가하세요.
- [X] 각 도메인 모델 별 레포지토리 인터페이스를 선언하세요.
    - 레포지토리 구현체(File, JCF)는 아직 구현하지 마세요. 이어지는 서비스 고도화 요구사항에 따라 레포지토리 인터페이스에 메소드가 추가될 수 있어요.

### DTO 활용하기
- [x]

### UserService 고도화
- 고도화
  - create
    - [x] 선택적으로 프로필 이미지를 같이 등록할 수 있습니다.
    - [x] DTO를 활용해 파라미터를 그룹화합니다.
      - 유저를 등록하기 위해 필요한 파라미터, 프로필 이미지를 등록하기 위해 필요한 파라미터 등
    - [x] username과 email은 다른 유저와 같으면 안됩니다.
    - [x] UserStatus를 같이 생성합니다.
  - find, findAll
    - DTO를 활용하여:
      - [x] 사용자의 온라인 상태 정보를 같이 포함하세요.
      - [X] 패스워드 정보는 제외하세요.
  - update
    - [x] 선택적으로 프로필 이미지를 대체할 수 있습니다.
    - [x] DTO를 활용해 파라미터를 그룹화합니다.
      - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
  - delete
    - [x] 관련된 도메인도 같이 삭제합니다.
      - BinaryContent(프로필), UserStatus <?>readStatus는 삭제 하지 않아도 되는건가?

### AuthService 구현
- login
  - [x] username, password과 일치하는 유저가 있는지 확인합니다.
    - [x] 일치하는 유저가 있는 경우: 유저 정보 반환
    - [x] 일치하는 유저가 없는 경우: 예외 발생
  - [x] DTO를 활용해 파라미터를 그룹화합니다.

### ChannelService 고도화
-  고도화
  - create
    - PRIVATE 채널과 PUBLIC 채널을 생성하는 메소드를 분리합니다.
    - [x] 분리된 각각의 메소드를 DTO를 활용해 파라미터를 그룹화합니다.
    - PRIVATE 채널을 생성할 때:
      - [x] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
      - [x] name과 description 속성은 생략합니다.
    - PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다.
  - find
    - DTO를 활용하여:
      - [X] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
      - [x] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
  - findAll
    - DTO를 활용하여:
      - [x] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다. // TODO Channel과 Message를 어떻게 연동할 것인가 를 다시 고려한 후 고칩니다
      - [x] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
    - [x] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId
    - [x] PUBLIC 채널 목록은 전체 조회합니다.
    - [x] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
  - update
    - [x] DTO를 활용해 파라미터를 그룹화합니다.
     - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
    - [x] PRIVATE 채널은 수정할 수 없습니다.
  - delete
    - [x] 관련된 도메인도 같이 삭제합니다.
      - Message, ReadStatus

### MessageService 고도화
- 고도화
- create
  - [x] 선택적으로 여러 개의 첨부파일을 같이 등록할 수 있습니다
  - [X] DTO를 활용해 파라미터를 그룹화합니다.
- findAll
  - [x] 특정 Channel의 Message 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findallByChannelId
- update
  - [x] DTO를 활용해 파라미터를 그룹화합니다.
    - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
- delete
  - [x] 관련된 도메인도 같이 삭제합니다.
    - 첨부파일(BinaryContent)


### ReadStatusService 구현
- create
  - [x] DTO를 활용해 파라미터를 그룹화합니다.
  - [X] 관련된  User가 존재하지 않으면 예외를 발생시킵니다.
  - [x] 같은 ChannelChannel이나과 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
- find
  - [x] id로 조회합니다.
-findAllByUserId
  - [x] userId를 조건으로 조회합니다.
- update
  - [x] DTO를 활용해 파라미터를 그룹화합니다. 
  - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터 => 수정할 값 파라미터는 readStatus 내부의 refresh 메서드로 대체
- delete
  - [x] id로 삭제합니다.

### UserStatusService 고도화
- create
  - [x] DTO를 활용해 파라미터를 그룹화합니다.
  - [x] 관련된 User가 존재하지 않으면 예외를 발생시킵니다.
  - [x] 같은 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
- find
  - [x] id로 조회합니다.
- findAll
  - [x] 모든 객체를 조회합니다.
- update
  - [x] DTO를 활용해 파라미터를 그룹화합니다.
  - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
- updateByUserId
  - [x] userId 로 특정 User의 객체를 업데이트합니다.
- delete
  - [x] id로 삭제합니다.
  
### BinaryContentService 구현
- create
  - [X] DTO를 활용해 파라미터를 그룹화합니다.
- find
  - [x] id로 조회합니다.
- findAllByIdIn
  - [x] id 목록으로 조회합니다.
- delete
  - [x] id로 삭제합니다.
  
### 새로운 도메인 Repository 구현체 구현
- [x] 지금까지 인터페이스로 설계한 각각의 Repository를 JCF, File로 각각 구현하세요.


### 심화 요구사항
### Bean 다루기
- [x]  Repository 구현체 중에 어떤 구현체를 Bean으로 등록할지 Java 코드의 변경 없이 application.yaml 설정 값을 통해 제어해보세요.
  - [x] discodeit.repository.type 설정값에 따라 Repository 구현체가 정해집니다.
    - [x] 값이 jcf 이거나 없으면 JCF*Repository 구현체가 Bean으로 등록되어야 합니다.
    - [x] 값이 file 이면 File*Repository 구현체가 Bean으로 등록되어야 합니다.

- [ ]  File*Repository 구현체의 파일을 저장할 디렉토리 경로를 application.yaml 설정 값을 통해 제어해보세요.


### 심화 요구 사항


## 주요 변경사항
1. Channel과 Message 도메인 모델에 있는 객체 참조에 대한 변경

   Channel 은 User owner를 참조

   Message는 Channel channel을 참조

   →

   Channel은 UUID ownerId 를 참조

   Message는 UUID channelId 을 참조한다.

   - 직접적인 의존성(Message는 Channel 있어야 생성되고, Channel은 User가 있어야 생성된다) 때문에 결합도가 높아져 변경에 대한 유연성이 부족하다고 생각했고,

   - 참조한 객체의 필드를 쓸 의도를 가지지 않은 상태이기에, 리소스 측면에서 낭비라는 생각과,

   - 이 채널의 주인이 누구인지, 이 메세지가 존재하는 채널이 어디인지 “가리키는 것”이 중요하기 때문에.
</BR>또한 “가리키는 것”만을 원하는데, 전체 객체를 참조해오는 것은 의도 자체의 모호성을 높일 수 있다고 생각했다.

   즉, **의존성 감소, 필요한 데이터만 불러오기, 의도를 명확하게 하기** 위해서 수정하고자 했고, 수정했습니다.


2. 서비스 분리

- channel에 참여한 유저 관리하기 -> Service 레이어에 ChannelUserService 서비스 추가 </BR>채널에 참여한 유저들을 관리하는 책임을 분리합니다. 이 서비스는 유저를 채널에 추가하거나 삭제하는 로직을 담당합니다.

- 지금 단계에서는 확장을 생각하지 않아 인터페이스는 정의하지 않았습니다.

3. 그외
User, Channel, Message 도메인 관련 모든 레이어의 deleteAll과 관련된 모든 것 삭제
MessageRepository findMessagesByChannelId() 메소드 추가


## 스크린샷

## 멘토에게

