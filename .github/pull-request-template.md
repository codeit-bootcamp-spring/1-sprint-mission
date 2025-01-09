## 요구사항

### 기본
### 프로젝트 초기화
- [x] IntelliJ를 통해 다음의 조건으로 Java 프로젝트를 생성합니다.
  - [x]  IntelliJ에서 제공하는 프로젝트 템플릿 중 Java를 선택합니다.
  - [x]  프로젝트의 경로는 스프린트 미션 리포지토리의 경로와 같게 설정합니다.
- [x]  Create Git Repository 옵션은 체크하지 않습니다.
- [x]  Build system은 Gradle을 사용합니다. Gradle DSL은 Groovy를 사용합니다.
- [x]  JDK 17을 선택합니다.
- [x]  GroupId는 com.sprint.mission로 설정합니다.
- [x]  ArtifactId는 수정하지 않습니다.
- [x]  .gitignore에 IntelliJ와 관련된 파일이 형상관리 되지 않도록 .idea디렉토리를 추가합니다.
### 도메인 모델링
- [x] 디스코드 서비스를 활용해보면서 각 도메인 모델에 필요한 정보를 도출하고, Java Class로 구현하세요.
  - [x] 패키지명: com.sprint.mission.discodeit.entity
  - [x] 도메인 모델 정의
    - [x] 공통
      - [x] id: 객체를 식별하기 위한 id로 UUID 타입으로 선언합니다.
      - [x] createdAt, updatedAt: 각각 객체의 생성, 수정 시간을 유닉스 타임스탬프로 나타내기 위한 필드로 Long 타입으로 선언합니다.
    - [x] User
    - [x] Channel
    - [x] Message
  - [x] 생성자
    - [x] id는 생성자에서 초기화하세요.
    - [x] createdAt는 생성자에서 초기화하세요.
    - [x] id, createdAt, updatedAt을 제외한 필드는 생성자의 파라미터를 통해 초기화하세요.
  - [x] 메소드
    - [x] 각 필드를 반환하는 Getter 함수를 정의하세요.
    - [x] 필드를 수정하는 update 함수를 정의하세요.
### 서비스 설계 및 구현
- [x] 도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요.
  - [x] 인터페이스 패키지명: com.sprint.mission.discodeit.service
  - [x] 인터페이스 네이밍 규칙: [도메인 모델 이름]Service
- [x] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
  - [x] 클래스 패키지명: com.sprint.mission.discodeit.service.jcf
  - [x] 클래스 네이밍 규칙: JCF[인터페이스 이름]
  - [x] Java Collections Framework를 활용하여 데이터를 저장할 수 있는 필드(data)를 final로 선언하고 생성자에서 초기화하세요.
  - [x] data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.
### 메인 클래스 구현
- [x] 메인 메소드가 선언된 JavaApplication 클래스를 선언하고, 도메인 별 서비스 구현체를 테스트해보세요.
  - [x] 등록
  - [x] 조회(단건, 다건)
  - [x] 수정
  - [x] 수정된 데이터 조회
  - [x] 삭제
  - [x] 조회를 통해 삭제되었는지 확인
### 기본 요구사항 커밋 태그
- [ ] 여기까지 진행 후 반드시 커밋해주세요. 그리고 sprint1-basic 태그를 생성해주세요.

### 심화
### 서비스 간 의존성 주입
- [x] 도메인 모델 간 관계를 고려해서 검증하는 로직을 추가하고, 테스트해보세요

## 주요 변경사항
- (X) 첫 PR


## 스크린샷
![image](https://github.com/k01zero/practice-project/blob/master/%EC%8A%A4%ED%94%84%EB%A6%B0%ED%8A%B8%20%EB%AF%B8%EC%85%98%201%20-%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%EA%B5%AC%EC%A1%B0%EB%B0%8F%20%EA%B8%B0%EB%8A%A5%EC%9D%84%20%EC%9C%84%ED%95%9C%20%EC%84%A4%EA%B3%84%20-%20%EC%8B%9C%ED%8A%B81-1.png?raw=true)
https://docs.google.com/spreadsheets/d/1jaSS_jD11BvTe0mmyEBVb5KBYNxTvuNifRDFtVUX208/edit?usp=sharing

## 멘토에게
- (질문 1) 사고의 과정을 어떻게 하면 될까요?(*이전에 이런 작업을 해본 경험이 없는 상태예요!)
<br/>
<br/>문제를 받았을 때 처음에는 적힌대로 코드를 작성하다가 중간에 이 프로젝트가 어떻게 돌아가는지 고민하게 되었어요.
<br/>
<br/>'사용자가 핸들링 하는 거라면, main에서 사용자가 직접 쓸 수 있는 건 User의 JCF 클래스의 메서드 밖에 없지 않을까?'
<br/>'하지만 개발자가 보는 부분이라면, User, Channel, Message의 JCF 클래스를 상관없이 접근해도 되겠지?'
<br/>
<br/> 처음에는 막연히 사용자가 쓰는건가? 생각하면서 작성하다가 위와 같은 고민을 하게 되어 중간에 코드를 교체하게 되었습니다.
그외에도 고려해야할 것들이 우르르 생각나서 정리를 못하겠다는 생각도 들었고,
<br/> 저는 여기서부터 혼란스러웠던 것 같은데, 보통 멘토님이나 다른 분들은 이런 문서를 읽을 때 
**어떤 것부터 생각해서, 이렇게 해야겠다** 라고 판단하시는지 그 사고의 과정이 궁금해요.
<br/>
<br/>
- (질문 2) 앞으로 개선해나가려면 어떻게 해야할까요?