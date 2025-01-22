## 요구사항

### 기본 요구사항
### File IO를 통한 데이터 영속화
- [X]  다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
  - [X]  클래스 패키지명: com.sprint.mission.discodeit.service.file
  - [X]  클래스 네이밍 규칙: File[인터페이스 이름]
  - [X]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.
- [X]  Application에서 서비스 구현체를 File*Service로 바꾸어 테스트해보세요.

### 서비스 구현체 분석
- [X] JCF*Service 구현체와 File*Service 구현체를 비교하여 공통점과 차이점을 발견해보세요
- 데이터 저장을 JCF로 하느냐 ".ser"로 하느냐, 그리고 저장할 때 이용되는 모듈들(JCF 내장 메서드, FileIO)의 차이
  - [X] "비즈니스 로직"과 관련된 코드를 식별해보세요.
  - [X] "저장 로직"과 관련된 코드를 식별해보세요.

### 레포지토리 설계 및 구현
- [X] "저장 로직"과 관련된 기능을 도메인 모델 별 인터페이스로 선언하세요.
  - [X] 인터페이스 패키지명: com.sprint.mission.discodeit.repository
  - [X] 인터페이스 네이밍 규칙: [도메인 모델 이름]Repository
- [X] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요.
  - [X] 클래스 패키지명: com.sprint.mission.discodeit.repository.jcf
  - [X] 클래스 네이밍 규칙: JCF[인터페이스 이름]
  - [X] 기존에 구현한 JCF*Service 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.
- [X] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요.
  - [X] 클래스 패키지명: com.sprint.mission.discodeit.repository.file
  - [X] 클래스 네이밍 규칙: File[인터페이스 이름]
  - [X] 기존에 구현한 File*Service 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.

### 심화 요구 사항
### 관심사 분리를 통한 레이어 간 의존성 주입
- [X] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
  - [X] 클래스 패키지명: com.sprint.mission.discodeit.service.basic
  - [X] 클래스 네이밍 규칙: Basic[인터페이스 이름]
  - [X] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.
  - [X] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.
  - [X] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)
- [X] Basic*Service 구현체를 활용하여 테스트해보세요
  - [X]  JCF*Repository  구현체를 활용하여 테스트해보세요.
  - [X]  File*Repository 구현체를 활용하여 테스트해보세요.
- [X] 이전에 작성했던 코드(JCF*Service 또는 File*Service)와 비교해 어떤 차이가 있는지 정리해보세요.
- JCF*Service, File*Service 둘다 Basic*Service의 조건으로 

## 주요 변경사항

## 스크린샷

## 멘토에게
!!!! 멘토님 이 부분이 가장 도움이 필요합니다 !!!!
아래 글은 강사님과 FileUserRepository에 있는 findUserById(UUID id) 메서드의 버그를 고친 내용입니다.

Junit을 통한 단위 테스트를 진행했고, FileUserServiceTest의 testUpdateUserNickname() 에서 진행했습니다.
이때 이미 존재하는 ser파일에 덮어씌워지지 않고 새로운 ser 파일이 생성되는 문제를 겪었습니다.
"이 문제는 이미 만들어진 객체를 새 객체에 덮어 씌우려고 하는데(제가 이해하긴 이랬습니다!),
BaseEntity에서 id를 final로 쓰고 있었기 때문에덮어씌워지지 않아 새로운 파일을 생성했다."
라는 문제였습니다.

===>

BaseEntity(도메인 모델) 에서 id의 final(11줄 : transient로 변경)을 변경하고, getId() 메서드에 로직을 추가하는 것으로 해결했습니다.

~~~~~~~

강사님 말씀으론, 이제 getId 부분도 로직이 추가됐으니까 일반적이지 않고 꼬여있는 부분이 있다고 합니다. 제가 아직 그런 부분들이 파악이 잘 안 되는데, 앞으로 어떻게 개선해야할까요?

[주로 봐야하는 파일들
FileUserRepository(레포지토리) - FileUserService(서비스 로직) - User(도메인 모델) - BaseEntity(도메인 모델) ]