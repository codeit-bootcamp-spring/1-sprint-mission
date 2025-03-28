package com.sprint.mission.discodeit.dto.user;

public record CreateUserRequest(
    String username,
    String email,
    String password
) {
  // record
  // 필드 유형과 이름만 필요한 불변 데이터 클래스
  // equals, hashcode, toString 메서드와 private, final field, public constructor
  // -> Java 컴파일러에 의해 생성됨
}