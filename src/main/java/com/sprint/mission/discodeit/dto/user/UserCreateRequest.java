package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 50, message = "최대 50자까지 입력할 수 있습니다. 일부 문자는 더 빨리 제한에 도달할 수 있어요.")
    String username,

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @Size(max = 100, message = "최대 100자까지 입력할 수 있습니다.")
    String email,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 60, message = "최대 60자까지 입력할 수 있습니다. 일부 문자는 더 빨리 제한에 도달할 수 있어요.")
    String password
) {
  // record
  // 필드 유형과 이름만 필요한 불변 데이터 클래스
  // equals, hashcode, toString 메서드와 private, final field, public constructor
  // -> Java 컴파일러에 의해 생성됨
}