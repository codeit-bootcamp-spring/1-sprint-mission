### 상황 :

java.lang.NullPointerException: Cannot invoke "java.util.UUID.toString()" because "id" is null

Postman으로 User를 생성하려고 하자, id가 null이라는 오류가 떴다.

### 문제 상황이 일어난 이유: 객체가 영속 상태가 되기 이전에 id를 참조했다.

User 객체를 생성할 때 프로필용 파일을 첨부하면 BinaryContent 객체도 함께 생성된다.

    binaryContentStorage.put(binaryContent.getId(), request.bytes());
    binaryContentRepository.save(binaryContent);

이 과정에서 ``binaryContent.getId()``를 호출하는데, binaryContet의 id는 JPA의 자동 생성 전략(@GeneratedValue)를 따르고 있다.
문제는 binaryContent가 영속(Persistent) 상태가 되기 전에 ``getId()``를 호출했다는 점이다.
JPA에서 ID는 엔티티가 영속 상태(Persist)가 되어야 할당되므로, 아직 ``save()``가 호출되지 않은 상태에서 ``id``가 ``null``이다.
따라서 binaryContentStorage 클래스의 ``resolvePath()`` 메서드 내부 ``binaryContent.getId().toString()`` 을 호출할 때
``NullPointerException``이 발생한 것이다.

### 해결 : save와 put의 순서를 바꿔 객체를 영속 상태로 만든 후 id를 참조했다.
