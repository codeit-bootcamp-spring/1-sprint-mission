# 매핑 정보 - 정리

| 엔티티 관계                               | 다중성 | 방향성                     | 부모-자식 관계                       | 연관관계의 주인   |
|--------------------------------------|-----|-------------------------|--------------------------------|------------|
| ReadStatus : User                    | 1:N | ReadStatus → User       | 부모: User, 자식: ReadStatus       | User       |
| ReadStatus : Channel                 | 1:N | ReadStatus → Channel    | 부모: Channel, 자식: ReadStatus    | Channel    |
| Message : User (author)              | N:1 | Message → User          | 부모: User, 자식: Message          | Message    |
| Message : BinaryContent(attachments) | N:M | Message → BinaryContent | 부모: Message, 자식: BinaryContent | Message    |
| Message : Channel                    | N:1 | Message → Channel       | 부모: Channel, 자식: Message       | Channel    |
| User : BinaryContent(profile)        | 1:1 | User → BinaryContent    | 부모: User, 자식: BinaryContent    | User       |
| User : UserStatus                    | 1:1 | User ↔ UserStatus       | 부모: User, 자식: UserStatus       | UserStatus |