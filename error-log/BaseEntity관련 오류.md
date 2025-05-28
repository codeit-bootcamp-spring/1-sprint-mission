### HibernateJpaConfiguration 가 엔티티 클래스에서 식별자(@Id 또는 @EnbeddedId)를 찾지 못하다

        [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]:
        Entity 'com.sprint.mission.discodeit.entity.base.BaseEntity' has no identifier 
        (every '@Entity' class must declare or inherit at least one '@Id' or '@EmbeddedId' property)

문제 상황 : Entity 클래스 Id 에 transient 키워드가 설정돼 있었다. 직렬화 <-> 역직렬화 요구 사항 과정의 잔재를 다 지우지 않았다.
왜 문제가 생기는가 : transient 키워드는 직렬화 작업에서 제외시킨다.
[JPA @Transient 어노테이션이 있는 필드 무시
](https://recordsoflife.tistory.com/579)
해결 : transient 삭제

----

### 싱글 테이블 전략에서는 @Table 어노테이션을 annotated 하지 못한다.

        2025-03-12T15:16:49.360+09:00 ERROR 48684 --- [           main] j.LocalContainerEntityManagerFactoryBean :
        Failed to initialize JPA EntityManagerFactory: Entity 'com.sprint.mission.discodeit.entity.BinaryContent'
        is a subclass in a 'SINGLE_TABLE' hierarchy and may not be annotated '@Table' (the root class declares the table mapping for the hierarchy)

문제 상황 : JPA의 SINGLE_TABLE 상속 전략에서 발생한 문제. 모든 상속 계층의 엔티티를 단일 테이블에 매핑한다.
왜 문제가 생기는가 : 상위 엔티티에서 이미 테이블 매핑(@Table)을 선언하기 때문에, 해당 계층의 하위 클래스인 BinaryContent는 별도로 @Table 어노테이션을
선언할 수 없다.
해결 : BaseEntity는 단순 공통 필드 제공 목적으로 이용되므로 @Entity
대신 [@MappedSuperclass 어노테이션](https://stufit-official.tistory.com/78)을 사용하여 엔티티 계층에 포함되지 않도록
한다.

