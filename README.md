# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리

## 데이터베이스

- [X] 아래와 같이 데이터베이스 환경을 설정하세요.
    - 데이터베이스 : discodeit
    - 유저 : discodeit_user
    - 패스워드 : discodeit1234
- [X] ERD 참고해 DDL 작성하고 테이블 생성

## Spring Data JPA 적용하기

- [ ] Spring Data JPA와 PostgreSQL을 위한 의존성을 추가하세요.
- [ ] 앞서 구성한 데이터베이스에 연결하기 위한 설정값을 application.yaml 파일에 작성하세요.
- [ ] 디버깅을 위해 SQL 로그와 관련된 설정값을 application.yaml 파일에 작성하세요.

## 엔티티 정의하기

- [ ]  클래스 다이어그램을 참고해 도메인 모델의 공통 속성을 추상 클래스로 정의하고 상속 관계를 구현하세요.
- [ ]  JPA의 어노테이션을 활용해 createdAt, updatedAt 속성이 자동으로 설정되도록 구현하세요
- [ ]  클래스 다이어그램을 참고해 클래스 참조 관계를 수정하세요. 필요한 경우 생성자, update 메소드를 수정할 수 있습니다. 단, 아직 JPA Entity와 관련된
  어노테이션은 작성하지 마세요.
- [ ]  ERD와 클래스 다이어그램을 토대로 연관관계 매핑 정보를 표로 정리해보세요.(이 내용은 PR에 첨부해주세요.)
- [ ] JPA 주요 어노테이션을 활용해 ERD, 연관관계 매핑 정보를 도메인 모델에 반영해보세요.
- [ ] ERD의 외래키 제약 조건과 연관관계 매핑 정보의 부모-자식 관계를 고려해 영속성 전이와 고아 객체를 정의하세요.

## 레포지토리와 서비스에 JPA 도입하기

- [ ] 기존의 Repository 인터페이스를 JPARepository로 정의하고 쿼리메소드로 대체하세요
- [ ] 영속성 컨텍스트의 특징에 맞추어 서비스 레이어를 수정해보세요

## DTO 적극 도입하기

- [ ] Entity를 Controller 까지 그대로 노출했을 때 발생할 수 있는 문제점에 대해 정리해보세요. DTO를 적극 도입했을 때 보일러플레이트 코드가 많아지지만,
  그럼에도 불구하고 어떤 이점이 있는지 알 수 있을거에요.(이 내용은 PR에 첨부해주세요.)
- [ ] 다음의 클래스 다이어그램을 참고하여 DTO를 정의하세요.
- [ ]  Entity를 DTO로 매핑하는 로직을 책임지는 Mapper 컴포넌트를 정의해 반복되는 코드를 줄여보세요.

## BinaryContent 저장 로직 고도화

- [ ]  BinaryContent 엔티티는 파일의 메타 정보(fileName, size, contentType)만 표현하도록 bytes 속성을 제거하세요.
- [ ]  BinaryContent의 byte[] 데이터 저장을 담당하는 인터페이스를 설계하세요.
- [ ]  서비스 레이어에서 기존에 BinaryContent를 저장하던 로직을 BinaryContentStorage를 활용하도록 리팩토링하세요.
- [ ]  BinaryContentController에 파일을 다운로드하는 API를 추가하고, BinaryContentStorage에 로직을 위임하세요.
- [ ]  로컬 디스크 저장 방식으로 BinaryContentStorage 구현체를 구현하세요.
- [ ]  discodeit.storage.type 값이 local 인 경우에만 Bean으로 등록되어야 합니다.

## 페이징과 정렬

- [ ] 메시지 목록을 조회할 때 다음의 조건에 따라 페이지네이션 처리를 해보세요
- [ ] 일관된 페이지네이션 응답을 위해 제네릭을 활용해 DTO로 구현하세요
- [ ] Slice 또는 Page 객체로부터 DTO를 생성하는 Mapper를 구현하세요