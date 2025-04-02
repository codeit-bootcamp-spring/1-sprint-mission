### 문제 상황 :

Strict stubbing argument mismatch. Please check:

- this invocation of 'toDto' method:
  userMapper.toDto(
  com.sprint.mission.discodeit.entity.User@743c6ce4
  );
  -> at com.sprint.mission.discodeit.service.basic.BasicUserService.createUser(
  BasicUserService.java:92)
- has following stubbing(s) with different arguments:
    1. userMapper.toDto(
       com.sprint.mission.discodeit.entity.User@30669dac
       );
       -> at com.sprint.mission.discodeit.service.UserServiceTest.createUser_Success(
       UserServiceTest.java:77)
       Typically, stubbing argument mismatch indicates user mistake when writing tests.
       Mockito fails early so that you can debug potential problem easily.
       However, there are legit scenarios when this exception generates false negative signal:
- stubbing the same method multiple times using 'given().will()' or 'when().then()' API
  Please use 'will().given()' or 'doReturn().when()' API for stubbing.
- stubbed method is intentionally invoked with different arguments by code under test
  Please use default or 'silent' JUnit Rule (equivalent of Strictness.LENIENT).
  For more information see javadoc for PotentialStubbingProblem class.
  org.mockito.exceptions.misusing.PotentialStubbingProblem:
  Strict stubbing argument mismatch. Please check:
- this invocation of 'toDto' method:
  userMapper.toDto(
  com.sprint.mission.discodeit.entity.User@743c6ce4
  );

### 문제가 일어난 이유 : User 객체가 테스트 코드에서 예상한 것과 다른 인수로 호출됐다.

Strict stubbing argument mismatch. Please check:
Strict stubbing argument mismatch. Please check:

- this invocation of 'toDto' method:
  userMapper.toDto(
  com.sprint.mission.discodeit.entity.User@743c6ce4
  );
  -> at com.sprint.mission.discodeit.service.basic.BasicUserService.createUser(
  BasicUserService.java:92)
- has following stubbing(s) with different arguments:
    1. userMapper.toDto(
       com.sprint.mission.discodeit.entity.User@30669dac
       );
       -> at com.sprint.mission.discodeit.service.UserServiceTest.createUser_Success(
       UserServiceTest.java:77)

실제로 호출된 userMapper.toDto() 메서드는 com.sprint.mission.discodeit.entity.User@743c6ce4 객체를 인자로 받았지만,
테스트에서 stubbing한 호출은 com.sprint.mission.discodeit.entity.User@30669dac 객체를 인자로 기대했습니다.

테스트에서 생성된 user 객체와 BasicUserService.createUser() 메서드 내부에서 생성되는 User 객체가 서로 다른 인스턴스이기 때문에 발생한 문제입니다.

### 해결 : user 객체가 expected 값과 동일한 값으로 stub될 수 있도록 설정해야 한다.

user 을 전달받던 userMapper.toDto() 메서드에 `any(User.class)` 를 전달한다.
테스트 의의에 맞게 변경했다.