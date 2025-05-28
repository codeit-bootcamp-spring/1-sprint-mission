
[![codecov](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission/branch/part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8/graph/badge.svg?token=KP1S8SQKNL)](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission)
[![CI workflow](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml/badge.svg?branch=part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8)](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml)

# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리
# 스프린트 미션 8


1. [목표](#목표)
2. [요구사항💟](#요구사항)
3. [나의 설계⭐](#나의-설계)

## 목표
- 데이터베이스 환경 설정 및 모델링
- Spring Data JPA 환경 적용
- Entity 연관 관계 매핑
- 레포지토리와 서비스 계층에 JPA 적용
- Transaction 처리
- 페이지네이션과 정렬
- DTO의 적극적인 도입과 MapStruct의 활용
- BinaryContent 저장 로직 고도화
  - 메타정보와 바이너리 정보 분리
- N+1 문제 해결


## 요구사항💟
### 기본

**데이터베이스**

- [x] 아래와 같이 데이터베이스 환경을 설정하세요.
데이터베이스: discodeit
유저: discodeit_user
패스워드: discodeit1234
- [x] ERD를 참고하여 DDL을 작성하고, 테이블을 생성하세요.
작성한 DDL 파일은 /src/main/resources/schema.sql 경로에 포함하세요.

---

**Spring Data JPA 적용하기**

- [x] Spring Data JPA와 PostgreSQL을 위한 의존성을 추가하세요.
- [x] 앞서 구성한 데이터베이스에 연결하기 위한 설정값을 application.yaml 파일에 작성하세요.
- [x] 디버깅을 위해 SQL 로그와 관련된 설정값을 application.yaml 파일에 작성하세요.

---

**엔티티 정의하기**

- [x]  클래스 다이어그램을 참고해 도메인 모델의 공통 속성을 추상 클래스로 정의하고 상속 관계를 구현하세요.
  - 이때 Serializable 인터페이스는 제외합니다.
  - 패키지명: com.sprint.mission.discodeit.entity.base
- [x]  JPA의 어노테이션을 활용해 createdAt, updatedAt 속성이 자동으로 설정되도록 구현하세요.
  - @CreatedDate, @LastModifiedDate
-  클래스 다이어그램을 참고해 클래스 참조 관계를 수정하세요. 
- [x]  ERD와 클래스 다이어그램을 토대로 연관관계 매핑 정보를 표로 정리해보세요.

![image](https://github.com/user-attachments/assets/e021bba9-a1cb-4c25-9b5b-8a2233c76362)

- [x] JPA 주요 어노테이션을 활용해 ERD, 연관관계 매핑 정보를 도메인 모델에 반영해보세요.
  - @Entity, @Table
  - @Column, @Enumerated
  - @OneToMany, @OneToOne, @ManyToOne
  - @JoinColumn, @JoinTable  
- [x] ERD의 외래키 제약 조건과 연관관계 매핑 정보의 부모-자식 관계를 고려해 영속성 전이와 고아 객체를 정의하세요.
  - cascade, orphanRemoval

---

**레포지토리와 서비스에 JPA 도입하기**

- [x] 기존의 Repository 인터페이스를 JPARepository로 정의하고 쿼리메소드로 대체하세요.
FileRepository와 JCFRepository 구현체는 삭제합니다.
- [x] 영속성 컨텍스트의 특징에 맞추어 서비스 레이어를 수정해보세요.
힌트: 트랜잭션, 영속성 전이, 변경 감지, 지연로딩

---

**DTO 적극 도입하기**

- [x] Entity를 Controller 까지 그대로 노출했을 때 발생할 수 있는 문제점에 대해 정리해보세요. DTO를 적극 도입했을 때 보일러플레이트 코드가 많아지지만, 그럼에도 불구하고 어떤 이점이 있는지 알 수 있을거에요.
 
```
📌 엔티티를 컨트롤러까지 그대로 노출했을 때 발생할 수 있는 문제점

엔티티가 그대로 노출된다는 것은 엔티티의 모든 속성이 공개될 수 있다는 뜻입니다.
1. 테이블 설계가 노출될 수 있어 보안 면에서 위험합니다.
2. 화면에서 필요한 데이터는 일부인데, 모든 속성을 다 보낼 필요는 없습니다. 엔티티 크기가 커질수록 클라이언트와의 통신 속도가 느려질 겁니다.
3. 새롭게 알게 된 문제점인데, JPA 양방향 관계를 갖고 있는 엔티티를 컨트롤러에서 조회하면 순환 참조 문제가 생길 수 있습니다. 
엔티티 A와 B가 있을 때(저희 미션에서는 User와 UserStatus가 예시가 될 것 같습니다.) 서비스에서 repository.findById()로 조회하면 엔티티 A가 조회되지만 지연 로딩으로 엔티티 B까지는 조회가 되지 않습니다. 하지만 컨트롤러에서 ResponseEntity로 엔티티 A를 내보내려고 할 때는 json화가 필요하니 엔티티 B까지 조회해야 되죠. 엔티티 B를 다시 json화하려고 하면 엔티티 A를 조회하게 되고, 무한 순환참조가 일어나게 된다고 이해를 했습니다.

참고: https://m.blog.naver.com/writer0713/221587351970

```

- [x]  Entity를 DTO로 매핑하는 로직을 책임지는 Mapper 컴포넌트를 정의해 반복되는 코드를 줄여보세요.

---

**BinaryContent 저장 로직 고도화**

데이터베이스에 이미지와 같은 파일을 저장하면 성능 상 불리한 점이 많습니다. 따라서 실제 바이너리 데이터는 별도의 공간에 저장하고, 데이터베이스에는 바이너리 데이터에 대한 메타 정보(파일명, 크기, 유형 등)만 저장하는 것이 좋습니다.

- [x]  BinaryContent 엔티티는 파일의 메타 정보(fileName, size, contentType)만 표현하도록 bytes 속성을 제거하세요.
- [x]  BinaryContent의 byte[] 데이터 저장을 담당하는 인터페이스를 설계하세요.
- [x]  서비스 레이어에서 기존에 BinaryContent를 저장하던 로직을 BinaryContentStorage를 활용하도록 리팩토링하세요.
- [x]  BinaryContentController에 파일을 다운로드하는 API를 추가하고, BinaryContentStorage에 로직을 위임하세요.
- [x]  로컬 디스크 저장 방식으로 BinaryContentStorage 구현체를 구현하세요.

---

**페이징과 정렬**

- [x] 메시지 목록을 조회할 때 다음의 조건에 따라 페이지네이션 처리를 해보세요.
50개씩 최근 메시지 순으로 조회합니다.
총 메시지가 몇개인지 알 필요는 없습니다.
- [x] 일관된 페이지네이션 응답을 위해 제네릭을 활용해 DTO로 구현하세요.
- [x] Slice 또는 Page 객체로부터 DTO를 생성하는 Mapper를 구현하세요.

### 심화

**N+1 문제**

- [x] N+1 문제가 발생하는 쿼리를 찾고 해결해보세요.
---

**읽기전용 트랜잭션 활용**

- [x] 프로덕션 환경에서는 OSIV를 비활성화하는 경우가 많습니다. 이때 서비스 레이어의 조회 메소드에서 발생할 수 있는 문제를 식별하고, 읽기 전용 트랜잭션을 활용해 문제를 해결해보세요.

---

**페이지네이션 최적화**
- [x] 오프셋 페이지네이션과 커서 페이지네이션 방식의 차이에 대해 정리해보세요.


📌 오프셋 페이지네이션
- DB에 오프셋 쿼리로 페이지 단위로 요청하고 응답 받습니다.
- 구현이 커서 페이지네이션보다는 쉬운 편입니다.
- 각각의 페이지를 요청하는 사이에 데이터의 변화가 있는 경우 중복된 데이터가 노출될 수 있습니다.
- 데이터row, offset이 크다면 성능에 부담이 됩니다. 

📌 커서 페이지네이션
- 클라이언트가 가져간 마지막 데이터를 커서로 받아 그 다음 데이터들을 응답합니다.
- 오프셋 페이지네이션의 문제가 해결됩니다. 오프셋만큼의 데이터를 스킵해서 응답할 데이터를 찾는 게 아니라 어느 데이터부터 확인해야 될지 커서가 명확하기 때문입니다.

- [x] 기존에 구현한 오프셋 페이지네이션을 커서 페이지네이션으로 리팩토링하세요.

---

**MapStruct 적용**

- [x] Entity와 DTO를 매핑하는 보일러플레이트 코드를 [MapStruct](https://mapstruct.org/) 라이브러리를 활용해 간소화해보세요.


## 나의 설계⭐



