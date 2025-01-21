# 프로젝트 목표

- Git과 GitHub을 통해 프로젝트를 관리할 수 있다.
- 채팅 서비스의 도메인 모델을 설계하고, Java로 구현할 수 있다.
- 인터페이스를 설계하고 구현체를 구현할 수 있다.
- 싱글톤 패턴을 구현할 수 있다.
- Java Collections Framework에 데이터를 생성/수정/삭제할 수 있다.
- Stream API를 통해 JCF의 데이터를 조회할 수 있다.
- [심화] 모듈 간 의존 관계를 이해하고 팩토리 패턴을 활용해 의존성을 관리할 수 있다.

# 프로젝트 마일스톤

1. 프로젝트 초기화 (Java, Gradle)
2. 도메인 모델 구현
3. 서비스 인터페이스 설계 및 구현체 구현
4. 각 도메인 모델별 CRUD
   - Java Collections Framework 기반
5. 의존성 주입

리드미 내용을 다시 작성했습니다. 아래의 내용으로 갱신되었습니다. 추가 요청이 있으면 알려주세요! 😊

---

# 요구사항

## 기본 요구사항

### 프로젝트 초기화

- [x] IntelliJ를 통해 다음의 조건으로 Java 프로젝트를 생성합니다.
  - [x] IntelliJ에서 제공하는 프로젝트 템플릿 중 Java를 선택합니다.
  - [x] 프로젝트의 경로는 스프린트 미션 리포지토리의 경로와 같게 설정합니다.
    - 예를 들어 스프린트 미션 리포지토리의 경로가 `/some/path/1-sprint-mission`이라면:
      - **Name**: 1-sprint-mission
      - **Location**: /some/path
  - [x] Create Git Repository 옵션은 체크하지 않습니다.
  - [x] Build system은 Gradle을 사용합니다. Gradle DSL은 Groovy를 사용합니다.
  - [x] JDK 17을 선택합니다.
  - [x] GroupId는 `com.sprint.mission`로 설정합니다.
  - [x] ArtifactId는 수정하지 않습니다.
  - [x] `.gitignore`에 IntelliJ와 관련된 파일이 형상관리 되지 않도록 `.idea` 디렉토리를 추가합니다.
    ```
    .idea
    ```

### 도메인 모델링

- [x] 디스코드 서비스를 활용해 각 도메인 모델에 필요한 정보를 도출하고, Java Class로 구현하세요.
  - [x] **패키지명**: `com.sprint.mission.discodeit.entity`
  - [x] **도메인 모델 정의**:
    - [x] **공통**:
      - [x] `id`: 객체를 식별하기 위한 UUID 타입 필드
      - [x] `createdAt`, `updatedAt`: 생성/수정 시간을 유닉스 타임스탬프로 나타내는 Long 타입 필드
    - [x] **User**
    - [x] **Channel**
    - [x] **Message**
  - [x] **생성자**:
    - [x] `id`는 생성자에서 초기화
    - [x] `createdAt`은 생성자에서 초기화
    - [x] `id`, `createdAt`, `updatedAt`을 제외한 필드는 생성자의 파라미터를 통해 초기화
  - [x] **메소드**:
    - [x] 각 필드를 반환하는 Getter 함수 정의
    - [x] 필드를 수정하는 Update 함수 정의

### 서비스 설계 및 구현

- [x] 도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요.
  - [x] **패키지명**: `com.sprint.mission.discodeit.service`
  - [x] **인터페이스 네이밍 규칙**: [도메인 모델 이름]Service
- [x] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
  - [x] **클래스 패키지명**: `com.sprint.mission.discodeit.service.jcf`
  - [x] **클래스 네이밍 규칙**: JCF[인터페이스 이름]
  - [x] Java Collections Framework를 활용하여 데이터를 저장할 수 있는 필드(data)를 final로 선언하고 생성자에서 초기화
  - [x] data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드 구현

### 메인 클래스 구현

- [x] 메인 메소드가 선언된 `JavaApplication` 클래스를 작성하고, 도메인 별 서비스 구현체를 테스트하세요.
  - [x] 등록
  - [x] 조회(단건, 다건)
  - [x] 수정
  - [x] 수정된 데이터 조회
  - [x] 삭제
  - [x] 조회를 통해 삭제 여부 확인

### 기본 요구사항 커밋 태그

- [x] 여기까지 진행 후 반드시 커밋해주세요. 그리고 `sprint1-basic` 태그를 생성해주세요.

## 심화 요구 사항

### 서비스 간 의존성 주입

- [x] 도메인 모델 간 관계를 고려해서 검증 로직 추가 및 테스트
  - 힌트: Message를 생성할 때 연관된 도메인 모델 데이터 확인하기

# 실행 결과

Java 프로젝트 실행 시 다음과 같이 출력됩니다:

```
/Users/*******/Library/Java/JavaVirtualMachines/jbr-17.0.12/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=54526:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/*******/Documents/1-sprint-mission/out/production/1-sprint-mission com.sprint.mission.discodeit.JavaApplication
Created User: User{id=60ffc3a2-5523-448d-8b0d-2a7315ca6bfa, username='Alice'}
Created Channel: Channel{id=7c3fad17-17f6-40bb-842a-a9b78eb163f0, name='General'}
Messages: [Message{id=cbda1336-b9d4-49e2-9c00-fdbc727b21e5, content='Hello, world!', userId=60ffc3a2-5523-448d-8b0d-2a7315ca6bfa, channelId=7c3fad17-17f6-40bb-842a-a9b78eb163f0}]

Process finished with exit code 0
```

