### EntityManagerFactory가 초기화하는데 실패했다. User & UserStatus

2025-03-12T15:26:02.432+09:00 ERROR 71408 --- [           main]
j.LocalContainerEntityManagerFactoryBean : Failed to initialize JPA EntityManagerFactory:
Association 'com.sprint.mission.discodeit.entity.User.userStatus' is 'mappedBy' a property named '
user_id' which does not exist in the target entity type '
com.sprint.mission.discodeit.entity.UserStatus'

'com.sprint.mission.discodeit.entity.User.userStatus' 가 user_id 란 이름의 속성으로 mappedBy되어야 하는데,
'com.sprint.mission.discodeit.entity.UserStatus'에는 'user_id'가 존재하지 않는다.

왜 문제가 일어나는가? :
UserStatus 객체에 user_id라는 이름을 가진 속성이 존재하지 않는데, User 측에는 user_id 필드를 찾아 접근하고 있기 때문이다.

해결 : mappedBy 속성에 연관관계 주인이 가진 필드명으로 지정해줬다.

        // mappedBy : 비주인 객체, 컬럼 만들지 마. 읽기 전용
        @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
        // 읽기 전용으로 만들고,
        // users 테이블에는 컬럼을 만들지 않는다.
        // mappedBy ->> userStatus의 user 필드에 본 객체가 참조되도록 만든다
        private UserStatus userStatus

----

| 항목           | 설명                                                                                         |
|--------------|--------------------------------------------------------------------------------------------|
| **mappedBy** | 연관 관계의 **주인(Owner)**이 아닌 쪽에서 사용                                                            |
| **역할**       | 주인 쪽에서 매핑된 필드를 지정하여, 양방향 관계에서 외래 키 컬럼을 데이터베이스에 생성하지 않도록 함. 주로 **읽기 전용** 역할을 함.             |
| **지정 방법**    | `mappedBy`에는 연관 관계가 정의된 **대상 엔티티의 필드 이름**을 지정                                              |
| **사용 예시**    | `User` 엔티티에서 `userStatus`를 `mappedBy = "user"`로 지정하여 `UserStatus`의 `user` 필드가 관계의 주인임을 나타냄 |
| **어디에서 사용**  | `@OneToOne`이나 `@OneToMany` 관계에서 사용. **주로 연관 관계의 주인(주로 외래 키를 가지는 엔티티)**을 가리킴                |
| **예시**       | `User`의 `userStatus` 필드에 `mappedBy = "user"`를 설정하면, `UserStatus`의 `user` 필드가 관계의 주인으로 설정됨  |
