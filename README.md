# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리
# 스프린트 미션1


1. [목표](#목표)
2. [요구사항💟](#요구사항)
3. [나의 설계⭐](#나의-설계)

## 목표
- Git과 GitHub을 통해 프로젝트를 관리할 수 있다.
- 채팅 서비스의 도메인 모델을 설계하고, Java로 구현할 수 있다.
- 인터페이스를 설계하고 구현체를 구현할 수 있다.
- 싱글톤 패턴을 구현할 수 있다.
- Java Collections Framework에 데이터를 생성/수정/삭제할 수 있다.
- Stream API를 통해 JCF의 데이터를 조회할 수 있다.
- [심화] 모듈 간 의존 관계를 이해하고 팩토리 패턴을 활용해 의존성을 관리할 수 있다.


## 요구사항💟
### 기본 요구사항
#### 도메인 모델링
- 디스코드 서비스를 활용해보면서 각 도메인 모델에 필요한 정보를 도출하고, Java Class로 구현하세요.
- 패키지명: com.sprint.mission.discodeit.entity
- 도메인 모델 정의
  - 공통
    - id: 객체를 식별하기 위한 id로 UUID 타입으로 선언합니다.
    - createdAt, updatedAt: 각각 객체의 생성, 수정 시간을 유닉스 타임스탬프로 나타내기 위한 필드로 Long 타입으로 선언합니다.
  - User
  - Channel
  - Message
  - 생성자
    - id는 생성자에서 초기화하세요.
    - createdAt는 생성자에서 초기화하세요.
    - id, createdAt, updatedAt을 제외한 필드는 생성자의 파라미터를 통해 초기화하세요.
  - 메소드
  - 각 필드를 반환하는 Getter 함수를 정의하세요.
  - 필드를 수정하는 update 함수를 정의하세요.
#### 서비스 설계 및 구현
-  도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요.
  - 인터페이스 패키지명: com.sprint.mission.discodeit.service
  - 인터페이스 네이밍 규칙: [도메인 모델 이름]Service
- 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
  - 클래스 패키지명: com.sprint.mission.discodeit.service.jcf
  - 클래스 네이밍 규칙: JCF[인터페이스 이름]
  - Java Collections Framework를 활용하여 데이터를 저장할 수 있는 필드(data)를 final로 선언하고 생성자에서 초기화하세요.
[ ] data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.

#### 메인 클래스 구현
- 메인 메소드가 선언된 JavaApplication 클래스를 선언하고, 도메인 별 서비스 구현체를 테스트해보세요.
  - 등록
  - 조회(단건, 다건)
  - 수정
  - 수정된 데이터 조회
  - 삭제
  - 조회를 통해 삭제되었는지 확인

## 나의 설계⭐

### 목표
- 싱글톤 패턴과 팩토리 패턴을 효율적으로 써보자.
- Stream API를 최대한 많이 써보자.
  
### 도메인 주요 사항
- src/main/java/discodeit/entity
- Common: 도메인들이 공통적으로 가진 속성을 정리한 클래스로 User, Channel, Message의 부모 클래스가 됩니다.
  - UUID id: 객체 식별용 아이디
  - Long createdAt: 객체 생성 시간
  - Long updatedAt: 객체 수정 시간
  - void updateClass(): 모든 인스턴스는 수정될 때마다 updatedAt이 현재 시간으로 변경되어야 합니다. updatedAt을 변경하는 메서드입니다.
- User: 사용자 클래스입니다.
  - String name: 사용자의 이름 ex) Alice
  - String email: 사용자의 이메일(계정), 중복 불가합니다.
  - List<Channel> channels: 사용자가 속한 채널 리스트
- Channel
  - String name: 채널의 이름
  - List<User> users: 채널에 속한 사용자 리스트
  - List<Message> messages: 채널에 속한 메세지 리스트
- Message
  - String content: 메세지의 내용
  - User sender: 메세지 작성자
  - Channel channel: 메세지의 소속 채널
    
### 인터페이스
CRUD 메소드를 선언하고, 각 메소드별 용도는 주석으로 추가했습니다.
- src/main/java/discodeit/service
- UserService: User와 관련된 모든 기능
- ChannelService: Channel과 관련된 모든 기능
- MessageService: Message와 관련된 모든 기능

### 서비스
각 인터페이스를 구현합니다.
- src/main/java/discodeit/service/jcf
- 모든 사용자, 채널, 메시지를 관리하는 관리 시스템으로 전역 객체로 선언되기 때문에 **싱글톤**을 적용합니다.
- JCFUserService
  - Map<UUID, User> data: 존재하는 모든 유저를 저장한 맵. User 식별자 id를 key로, 해당 User를 value로 하는 맵. 서비스 계층에서 객체 id를 이용해 메서드를 호출하고 싶어 id를 이용했습니다.
- JCFChannelService
  - Map<UUID, Channel> data: 존재하는 모든 채널을 저장한 맵.
- JCFMessageService
  - Map<UUID, Message> data: 존재하는 모든 메세지를 저장한 맵.

  
### 팩토리 패턴
- ServiceFactory에서 모든 JCF[Domain]Service를 관리합니다.
- 각 JCF[Domain]Service 인스턴스를 가지고 있습니다.
- 인스턴스 생성 후 의존성을 주입합니다.
