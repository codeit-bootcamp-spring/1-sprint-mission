## 요구사항

### 기본 요구사항

### API 명세

    - 이번 미션은 아래의 API 스펙과 비교하며 구현해보세요.   
    - API 스펙 v1.1
    - 정적 리소스 v1.1
    - 소스 코드(참고용) v1.1

### 데이터베이스

- [X] 아래와 같이 데이터베이스 환경을 설정하세요.
    - 데이터베이스: discodeit
    - 유저: discodeit_user
    - 패스워드: discodeit1234
- [x] ERD를 참고하여 DDL을 작성하고, 테이블을 생성하세요.
    - 작성한 DDL 파일은 /src/main/resources/schema.sql 경로에 포함하세요.
        - PK: Primary Key
        - UK: Unique Key
        - NN: Not Null
        - FK: Foreign Key
            - ON DELETE CASCADE: 연관 엔티티 삭제 시 같이 삭제
            - ON DELETE SET NULL: 연관 엔티티 삭제 시 NULL로 변경

### Spring Data JPA 적용하기

- [x] Spring Data JPA와 PostgreSQL을 위한 의존성을 추가하세요.
- [x] 앞서 구성한 데이터베이스에 연결하기 위한 설정값을 application.yaml 파일에 작성하세요.
- [x] 디버깅을 위해 SQL 로그와 관련된 설정값을 application.yaml 파일에 작성하세요.

### 엔티티 정의하기

- [X]  클래스 다이어그램을 참고해 도메인 모델의 공통 속성을 추상 클래스로 정의하고 상속 관계를 구현하세요.
- 이때 Serializable 인터페이스는 제외합니다.
- 패키지명: com.sprint.mission.discodeit.entity.base
- 클래스 다이어그램 (xs6bzcvs6-image.png)

- [x]  JPA의 어노테이션을 활용해 createdAt, updatedAt 속성이 자동으로 설정되도록 구현하세요.
- @CreatedDate, @LastModifiedDate

- [x]  클래스 다이어그램을 참고해 클래스 참조 관계를 수정하세요. 필요한 경우 생성자, update 메소드를 수정할 수 있습니다. 단, 아직 JPA Entity와 관련된
  어노테이션은 작성하지 마세요.
- 클래스 다이어그램(pq5iz92wt-image.png)
    - 화살표의 방향과 화살표 유무에 유의하세요.

- [X]  ERD와 클래스 다이어그램을 토대로 연관관계 매핑 정보를 표로 정리해보세요.(이 내용은 PR에 첨부해주세요.)

| 엔티티 관계                | 다중성  | 방향성                          | 부모-자식 관계                       | 연관관계의 주인   |
|-----------------------|------|------------------------------|--------------------------------|------------|
| User:UserStatus       | 	1:1 | User↔UserStatus 양방향	         | 부모: User, 자식: UserStatus	      | UserStatus |
| User:ReadStatus       | 1:N  | ReadStatus → User 단방향        | 부모: User, 자식: ReadStatus       | ReadStatus |
| User:BinaryContent    | 1:1  | User → BinaryContent  단방향    | 부모: BinaryContent, 자식: User    | User       |
| User:Message          | 1:N  | Message → User  단방향          | 부모: User, 자식: Message          | Message    |
| ReadStatus:Channel    | 1:1  | ReadStatus → Channel  단방향    | 부모: Channel, 자식: ReadStatus    | ReadStatus |
| Channel : Message     | 1:N  | Message→ Channel  단방향        | 부모: Channel, 자식: Message       | Message    |
| Message:BinaryContent | 1:N  | Message → BInaryContent  단방향 | 부모: BInaryContent, 자식: Message | Message    |

- [x] JPA 주요 어노테이션을 활용해 ERD, 연관관계 매핑 정보를 도메인 모델에 반영해보세요.
    - @Entity, @Table
    - @Column, @Enumerated
    - @OneToMany, @OneToOne, @ManyToOne
    - @JoinColumn, @JoinTable

        - 엔티티와 분리해 다른 레이어에 둘 수 있는 로직은 분리, 그렇지 않은 로직은 남겨두는 식으로 진행했습니다.
        - 엔티티에 있던 유효성 검증 로직을 제거했습니다.
          JPA를 통해 DB와 교류할 때, 엔티티는 **데이터베이스 테이블과 매핑되는 역할**만 담당하므로,
          **데이터 구조에만 집중하는 것**이 **책임 분리 관점**에서 더 깔끔하다고 생각했습니다.
        - 그러나 UserStatus의 isOnline() 처럼 엔티티의 상태를 판단하는 메서드는 엔티티 내에서 유지하는 것이 적합하고, 코드 자체가 간결해 엔티티 내부에
          포함해두는 게 맞다 생각돼 그대로 배치해뒀습니다.

        - 데이터를 전달받을 때
            - 가독성, 유연함, 불변 객체 생성, 롬복 어노테이션 등의 이점을 고려해 **빌더 패턴**을 선택


- [x] ERD의 외래키 제약 조건과 연관관계 매핑 정보의 부모-자식 관계를 고려해 영속성 전이와 고아 객체를 정의하세요.
    - cascade, orphanRemoval

### 레포지토리와 서비스에 JPA 도입하기

- [x] 기존의 Repository 인터페이스를 JPARepository로 정의하고 쿼리메소드로 대체하세요.
    - FileRepository와 JCFRepository 구현체는 삭제합니다.
- [x] 영속성 컨텍스트의 특징에 맞추어 서비스 레이어를 수정해보세요.
    - 힌트: 트랜잭션, 영속성 전이, 변경 감지, 지연로딩

### DTO 적극 도입하기

- [X] Entity를 Controller 까지 그대로 노출했을 때 발생할 수 있는 문제점에 대해 정리해보세요. DTO를 적극 도입했을 때 보일러플레이트 코드가 많아지지만,
  그럼에도 불구하고 어떤 이점이 있는지 알 수 있을거에요.(이 내용은 PR에 첨부해주세요.)
    - 힌트
        - Entity와 API의 결합
        - 프로덕션 환경에서는 성능을 고려해 OSIV를 false로 설정하는 경우가 대부분
        - 양방향 연관관계 시 순환 참조
        - 민감한 데이터

---

        Entity와 API의 결합
            - API 스펙이 도메인 모델(그 중 Entity)에 종속된다.
            - Entity 구조 변경시 API 응답도 변경될 가능성이 크다.
                => 클라이언트가 Entity 구조에 의존하게 되어 유지보수가 어려워진다.

        OSIV(Open Session In View)* 설정
            - spring.jpa.open-in-view=false 로 설정해두면,
                - Controller에서 Lazy Loading된 필드를 조회할 때 LazyInitializationException 이 발생할 수도 있다.
                - (DTO를 사용하면 필요한 데이터만 미리 조회해서 문제 방지 가능)

            * OSIV란 영속성 컨텍스트를 Controller까지 열어두는 설정
            true일시 Controller에서 Entity의 Lazy 필드를 접근할 수 있다.

        순환 참조
            - 양방향 연관관계를 가진 엔티티 -> JSON 으로 변환할 때 StackOverflow 가 발생할 수도 있다.
            - (DTO를 사용하면 필요한 데이터만 선택적으로 제공해서 순환 참조 문제 해결)

        민감한 데이터 노출 위험
            - Entity 에 노출되면 안 되는 데이터가 포함되어있을 수 있다.
            - (DTO를 사용하면 필요한 데이터만 선별적으로 제공 가능하여 보안성 강화)

---

- [x] 다음의 클래스 다이어그램을 참고하여 DTO를 정의하세요.(hd4c6g1of-image.png)
- [x]  Entity를 DTO로 매핑하는 로직을 책임지는 Mapper 컴포넌트를 정의해 반복되는 코드를 줄여보세요.(buo7cmjvp-image.png)
    - 패키지명: com.sprint.mission.discodeit.mapper

### BinaryContent 저장 로직 고도화

데이터베이스에 이미지와 같은 파일을 저장하면 성능 상 불리한 점이 많습니다.
따라서 실제 바이너리 데이터는 별도의 공간에 저장하고, 데이터베이스에는 바이너리 데이터에 대한 메타
정보(파일명, 크기, 유형 등)만 저장하는 것이 좋습니다.

- [X]  BinaryContent 엔티티는 파일의 메타 정보(fileName, size, contentType)만 표현하도록 bytes 속성을 제거하세요.
- [X]  BinaryContent의 byte[] 데이터 저장을 담당하는 인터페이스를 설계하세요.
  저장 매체의 확장성(로컬 저장소, 원격 저장소)을 고려해 인터페이스부터 설계합니다.
- 패키지명: com.sprint.mission.discodeit.storage
- 클래스 다이어그램(nqt5zw2pk-image.png)
- BinaryContentStorage
    - 바이너리 데이터의 저장/로드를 담당하는 컴포넌트입니다.
    - UUID put(UUID, byte[])
        - UUID 키 정보를 바탕으로 byte[] 데이터를 저장합니다.
        - UUID는 BinaryContent의 Id 입니다.
    - InputStream get(UUID)
        - 키 정보를 바탕으로 byte[] 데이터를 읽어 InputStream 타입으로 반환합니다.
        - UUID는 BinaryContent의 Id 입니다.
    - ResponseEntity<?> download(BinaryContentDto)
        - HTTP API로 다운로드 기능을 제공합니다.
        - BinaryContentDto 정보를 바탕으로 파일을 다운로드할 수 있는 응답을 반환합니다.
- [x]  서비스 레이어에서 기존에 BinaryContent를 저장하던 로직을 BinaryContentStorage를 활용하도록 리팩토링하세요.

- [x]  BinaryContentController에 파일을 다운로드하는 API를 추가하고, BinaryContentStorage에 로직을 위임하세요.

- 엔드포인트: GET /api/binaryContents/{binaryContentId}/download
    - 요청
        - 값: BinaryContentId // 전달받아야 하는 값
        - 방식: Query Parameter
          // 이 부분은 의도 파악이 안됩니다 무슨 말인지?!...
          Id를 이미 Path에서 받고 있는데 방식은 쿼리 파라미터를 받아오라는 게?

    - 응답: ResponseEntity<?>
    - 클래스 다이어그램 (5qwe2kqno-image.png)

- [x]  로컬 디스크 저장 방식으로 BinaryContentStorage 구현체를 구현하세요.
    - 클래스 다이어그램 (skptrmm5p-image.png)
    - [x]  discodeit.storage.type 값이 local 인 경우에만 Bean으로 등록되어야 합니다.
        - > application.yml(애플리케이션 속성 모음) type에 local 로 지정, Appconfig 을 통해 Bean을 지정
    - [x] Path root
        - 로컬 디스크의 루트 경로입니다.
        - discodeit.storage.local.root-path 설정값을 정의하고, 이 값을 통해 주입합니다.
        - > 직접 코드에서 지정하는 게 아니라 appliaiton.yml에 루트 지정 + yml 속성 값 불러와서 이용
    - [x] void init()
        - 루트 디렉토리를 초기화합니다. // @PostConstruct 어노테이션 이용
        - Bean이 생성되면 자동으로 호출되도록 합니다.
    - [x] Path resolvePath(UUID)
        - 파일의 실제 저장 위치에 대한 규칙을 정의합니다.
            - 파일 저장 위치 규칙 예시: {root}/{UUID}
        - put, get 메소드에서 호출해 일관된 파일 경로 규칙을 유지합니다.
    - [x] ResponseEntity<Resource> donwload(BinaryContentDto)
        - get 메소드를 통해 파일의 바이너리 데이터를 조회합니다.
        - BinaryContentDto와 바이너리 데이터를 활용해 ResponseEntity<Resource> 응답을 생성 후 반환합니다.

### 페이징과 정렬

- [x] 메시지 목록을 조회할 때 다음의 조건에 따라 페이지네이션 처리를 해보세요.
    - 50개씩 최근 메시지 순으로 조회합니다.
    - 총 메시지가 몇개인지 알 필요는 없습니다.

- [X] 일관된 페이지네이션 응답을 위해 제네릭을 활용해 DTO로 구현하세요.
    - 패키지명: com.sprint.mission.discodeit.dto.response
    - 클래스 다이어그램 (wj4q7nhn3-image.png)
        - content: 실제 데이터입니다.
        - number: 페이지 번호입니다.
        - size: 페이지의 크기입니다.
        - totalElements: T 데이터의 총 갯수를 의미하며, null일 수 있습니다.

- [X] Slice 또는 Page 객체로부터 DTO를 생성하는 Mapper를 구현하세요.
    - 패키지명: com.sprint.mission.discodeit.mapper
    - (x7qjncxm0-image.png)
    - 확장성을 위해 제네릭 메소드로 구현하세요.

### 심화 요구사항

### N+1 문제

- [ ] N+1 문제가 발생하는 쿼리를 찾고 해결해보세요.

### 읽기전용 트랜잭션 활용

- [ ] 프로덕션 환경에서는 OSIV를 비활성화하는 경우가 많습니다. 이때 서비스 레이어의 조회 메소드에서 발생할 수 있는 문제를 식별하고, 읽기 전용 트랜잭션을 활용해 문제를
  해결해보세요.

  OSIV 비활성화하기

            spring:
            jpa:
            open-in-view: false

### 페이지네이션 최적화

- [ ] 오프셋 페이지네이션과 커서 페이지네이션 방식의 차이에 대해 정리해보세요.

### MapStruct 적용

- [X] Entity와 DTO를 매핑하는 보일러플레이트 코드를 MapStruct 라이브러리를 활용해 간소화해보세요.

## 주요 변경사항

1. Channel-User 도메인 삭제
   그리고 삭제하면서 생긴 문제들 전체적으로 리팩토링

   베이스 코드에서는 Channel-User 관계 도메인 대신 ReadStatus를 통해서 Channel:User (N:N)관계를 Channel:ReadStatus (1:N),
   User:ReadStatus (1:N) 로 분리한 것을 확인하고, 유사하게 수정했습니다.
   *Channel-User도, ReadStatus도 User, Channel을 참조한다는 점에서 동일한 것으로 판단했고, ReadStatus 쪽을 재활용한 것으로 이해했습니다.
   ReadStatus 도메인의 역할은 "유저가 채널의 메세지를 읽었느냐" 이므로,
   효율의 관점에서는 ReadStatus를 재활용이 이득이고, 책임분리 및 유지보수 관점에서는 Channel-User과 같은 관계 도메인을 생성하는 게 옳다고 느껴집니다.

## 스크린샷

## 멘토에게

User, Channel 를 생성하는 테스트만 진행했고
Message, BinaryContent, ReadStatus, Auth 테스트를 진행못해봤습니다.
Message의 경우 id가 null로 전달된다는 오류가 뜨는데, 깊게 들어가서 어느 부분이 문제인지 아직 파악하지 못했습니다.
