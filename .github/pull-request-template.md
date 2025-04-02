## 요구사항

### 기본 요구사항

### 프로파일 기반 설정 관리

- [x] 개발, 운영 환경에 대한 프로파일을 구성하세요.
    - [x] application-dev.yaml, application-prod.yaml 파일을 생성하세요.
    - [x] 다음과 같은 설정값을 프로파일별로 분리하세요.
        - [x] 데이터베이스 연결 정보
        - [x] 서버 포트

### 로그 관리

- [x] Lombok의 @Slf4j 어노테이션을 활용해 로깅을 쉽게 추가할 수 있도록 구성하세요.
- [x] application.yaml에 기본 로깅 레벨을 설정하세요.
    - 기본적으로 info 레벨로 설정합니다.
- [x] 환경 별 적절한 로깅 레벨을 프로파일 별로 설정해보세요.
    - SQL 로그를 보기위해 설정했던 레벨은 유지합니다.
    - 우리가 작성한 프로젝트의 로그는 개발 환경에서 debug, 운영 환경에서는 info 레벨로 설정합니다.
- [x] Spring Boot의 기본 로깅 구현체인 Logback의 설정 파일을 구성하세요.
    - [x]  logback-spring.xml 파일을 생성하세요.
    - [x]  다음 예시와 같은 로그 메시지를 출력하기 위한 로깅 패턴과 출력 방식을 커스터마이징하세요.

      - 로그 출력 예시

        # 패턴

        {년}-{월}-{일} {시}:{분}:{초}:{밀리초} [{스레드명}] {로그 레벨(5글자로 맞춤)} {로거 이름(최대 36글자)} - {로그 메시지}{줄바꿈}

        # 예시

        25-01-01 10:33:55.740 [main] DEBUG c.s.m.discodeit.DiscodeitApplication - Running with Spring Boot
        v3.4.0, Spring v6.2.0

    - ![img_4.png](img_4.png)

    - [x]  콘솔과 파일에 동시에 로그를 기록하도록 설정하세요.
      - [x] 파일은 {프로젝트 루트}/.logs 경로에 저장되도록 설정하세요.
    - [x]  로그 파일은 일자별로 롤링되도록 구성하세요.
    - [x]  로그 파일은 30일간 보관하도록 구성하세요.

- [x] 서비스 레이어와 컨트롤러 레이어의 주요 메소드에 로깅을 추가하세요.
    - [x] 로깅 레벨을 적절히 사용하세요: ERROR, WARN, INFO, DEBUG
    - [x] 다음과 같은 메소드에 로깅을 추가하세요:
        - [x] 사용자 생성/수정/삭제
        - [x] 채널 생성/수정/삭제
        - [x] 메시지 생성/수정/삭제
        - [x] 파일 업로드/다운로드

### 예외 처리 고도화

- [x]  커스텀 예외를 설계하고 구현하세요.
    - 패키지명: com.sprint.mission.discodeit.exception[.{도메인}]
    - [X]  ErrorCode Enum 클래스를 통해 예외 코드명과 메시지를 정의하세요.
        - 아래는 예시입니다. 필요하다고 판단되는 다양한 코드를 정의하세요.
        - 예시 (6ag3lzl9i-image.png)
    - [x]  모든 예외의 기본이 되는 DiscodeitException 클래스를 정의하세요.
        - 클래스 다이어그램 (j5vtp941a-image.png)
        - details는 예외 발생 상황에 대한 추가정보를 저장하기 위한 속성입니다.
            - 예시
            - 조회 시도한 사용자의 ID 정보
            - 업데이트 시도한 PRIVATE 채널의 ID 정보
    - [x]  DiscodeitException을 상속하는 주요 도메인 별 메인 예외 클래스를 정의하세요.
        - UserException, ChannelException 등
        - 실제로 활용되는 클래스라기보다는 예외 클래스의 계층 구조를 명확하게 하기 위한 클래스 입니다.
    - [x]  도메인 메인 예외 클래스를 상속하는 구체적인 예외 클래스를 정의하세요.
        - UserNotFoundException, UserAlreadyExistException 등 필요한 예외를 정의하세요.
        - 예시(a6f585icy-image.png)
- [X]  기존에 구현했던 예외를 커스텀 예외로 대체하세요.
    - NoSuchElementException
    - IllegalArgumentException
    - …
- [x]  ErrorResponse를 통해 일관된 예외 응답을 정의하세요.
    - 클래스 다이어그램 (3gqeampkw-image.png)
        - int status: HTTP 상태코드
        - String exceptionType: 발생한 예외의 클래스 이름
- [x]  앞서 정의한 ErrorResponse와 @RestControllerAdvice를 활용해 예외를 처리하는 예외 핸들러를 구현하세요.
    - 모든 핸들러는 일관된 응답(ErrorResponse)을 가져야 합니다.

### 유효성 검사

- [x] Spring Validation 의존성을 추가하세요.
- [X] 주요 Request DTO에 제약 조건 관련 어노테이션을 추구하세요.
    - @NotNull, @NotBlank, @Size, @Email 등
- [X] 컨트롤러에 @Valid 를 사용해 요청 데이터를 검증하세요.
- [x] 검증 실패 시 발생하는 MethodArgumentNotValidException을 전역 예외 핸들러에서 처리하세요.
- [x] 유효성 검증 실패 시 상세한 오류 메시지를 포함한 응답을 반환하세요.
  errorResponse 로 반환하도록 해놓는다.

### Actuator

- [x] Spring Boot Actuator 의존성을 추가하세요.
- [x] 기본 Actuator 엔트포인트를 설정하세요.
    - health, info, metrics, loggers
- [x] Actuator info를 위한 애플리케이션 정보를 추가하세요.
    - 애플리케이션 이름: Discodeit
    - 애플리케이션 버전: 1.7.0
    - 자바 버전: 17
    - 스프링 부트 버전: 3.4.0
    - 주요 설정 정보
        - 데이터소스: url, 드라이버 클래스 이름
        - jpa: ddl-auto
        - storage 설정: type, path
        - multipart 설정: max-file-size, max-request-size
- [x] Spring Boot 서버를 실행 후 각종 정보를 확인해보세요.
    - /actuator/info
    - /actuator/metrics
    - /actuator/health
    - /actuator/loggers

### 단위 테스트

- [X] 서비스 레이어의 주요 메소드에 대한 단위 테스트를 작성하세요.
    - [X] 다음 서비스의 핵심 메소드에 대해 각각 최소 2개 이상(성공, 실패)의 테스트 케이스를 작성하세요.
        - [x] UserService: create, update, delete 메소드
        - [X] ChannelService: create(PUBLIC, PRIVATE), update, delete, findByUserId 메소드
        - [x] MessageService: create, update, delete, findByChannelId 메소드
    - [x] Mockito를 활용해 Repository 의존성을 모의(mock)하세요.
    - [ ] BDDMockito를 활용해 테스트 가독성을 높이세요.

### 슬라이스 테스트

- [x] 레포지토리 레이어의 슬라이스 테스트를 작성하세요.
    - [x] @DataJpaTest를 활용해 테스트를 구현하세요.
    - [x] 테스트 환경을 구성하는 프로파일을 구성하세요.
        - [x] application-test.yaml을 생성하세요.
        - [x] 데이터소스는 H2 인메모리 데이터 베이스를 사용하고, PostgreSQL 호환 모드로 설정하세요.
        - [x] H2 데이터베이스를 위해 필요한 의존성을 추가하세요.
        - [x] 테스트 시작 시 스키마를 새로 생성하도록 설정하세요.
        - [x] 디버깅에 용이하도록 로그 레벨을 적절히 설정하세요.
    - [x] 테스트 실행 간 test 프로파일을 활성화 하세요.
        - [X] JPA Audit 기능을 활성화 하기 위해 테스트 클래스에 @EnableJpaAuditing을 추가하세요.
        - [x] 주요 레포지토리(User, Channel, Message)의 주요 쿼리 메소드에 대해 각각 최소 2개 이상(성공, 실패)의 테스트 케이스를 작성하세요.
            - [x] 커스텀 쿼리 메소드
            - [x] 페이징 및 정렬 메소드
            - (channel 엔 두 개 다 없어서 x)
- [X] 컨트롤러 레이어의 슬라이스 테스트를 작성하세요.
    - [x]  @WebMvcTest를 활용해 테스트를 구현하세요.
    - [X]  WebMvcTest에서 자동으로 등록되지 않는 유형의 Bean이 필요하다면 @Import를 활용해 추가하세요.
        - 예시 @Import({ErrorCodeStatusMapper.class})
    - [△]  주요 컨트롤러(User, Channel, Message)에 대해 최소 2개 이상(성공, 실패)의 테스트 케이스를 작성하세요.
    - [X]  MockMvc를 활용해 컨트롤러를 테스트하세요.
    - [X]  서비스 레이어를 모의(mock)하여 컨트롤러 로직만 테스트하세요.
    - [X]  JSON 응답을 검증하는 테스트를 포함하세요.

----

### 통합 테스트

- [X] 통합 테스트 환경을 구성하세요.
    - [X] @SpringBootTest를 활용해 Spring 애플리케이션 컨텍스트를 로드하세요.
    - [X] H2 인메모리 데이터베이스를 활용하세요.
    - [X] 테스트용 프로파일을 구성하세요.
- [ ] 주요 API 엔드포인트에 대한 통합 테스트를 작성하세요.
    - [ ] 주요 API에 대해 최소 2개 이상의 테스트 케이스를 작성하세요.
        - [ ] 사용자 관련 API (생성V, 수정, 삭제, 목록 조회)
        - [ ] 채널 관련 API (생성(공개V), 수정, 삭제V)
        - [ ] 메시지 관련 API (생성, 수정V, 삭제, 목록 조회)
    - [x] 각 테스트는 @Transactional을 활용해 독립적으로 실행하세요.

----

### 심화 요구사항

## 주요 변경사항

1. 메시지(Message) 기능이 동작할 수 있도록 변경

- Entity 설정 문제로 정상적으로 동작하지 않았음.

## 스크린샷

## 멘토에게

1. 서비스 단 로깅 (Service Logging)

- 비즈니스 로직의 핵심 이벤트를 기록
- 검증 로직에서 문제 발생 시 WARN 레벨로 로깅
- 현재 INFO에서는 ID를 남기지 않도록 설정했으나, 어떤 방식이 적절한 걸까요...? 노출해도 되는 정보와 아닌 정보가 궁금합니다!

2. warn과 error

- "이미 존재하는 이름"과 같은 곳은 warn을, "유저를 찾지 못함" 에는 error을 설정했는데, 두루뭉싫하게만 느껴지고 아직 정확히 어디에 warn 을 쓰고 error 을
  써야하는지 구분이 잘 되지 않습니다. 정의 외의 이해하기 쉬운 기준이 있을까요?

3. base7 코드 관련 질문

- base7 코드에서 확인해보니 User 를 생성할 때 UserStatus, BinaryContent 를 생성하는데, Service 단의 create 로직을 들고오지 않고 직접
  도메인을 생성하는 것으로 확인했는데, 어떤 방법이 더 효율적인지도 궁금하고 멘토님이라면 둘 중 어떤 방법을 택하는지도 궁급합니다.
  Service 단의 로직은 DTO 를 반환해서, 해당 방법을 이용할시 User 필드에 등록하기 위해 toEntity 라는 변환 작업이 필요하다는 생각을 했었는데, 그 방법보다
  base7 코드에서 제공하는 방식이 보다 편하다고 느껴지기도 하면서 그렇다면 Service 단의 create 로직을 사용하지 않는데, 왜 해당 메서드를 제작한 걸까? 하는
  생각도 듭니다.

- base 7 코드에서는 user 에 userStatus 를 지정하지 않던데
  저는 위와 같이 했을 때 toDto 쪽에서 문제가 생기던데,
  코드를 어떻게 이해보면 될까요?

