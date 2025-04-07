package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

  @Getter
  @NoArgsConstructor
  public static class Create {

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, message = "이름은 최소 2글자 이상이어야 합니다.")
    private String username;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;
  }

  @Getter
  @NoArgsConstructor
  public static class Update {

    @Size(min = 2, message = "이름은 최소 2글자 이상이어야 합니다.")
    private String username;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;
  }

  @Getter
  @NoArgsConstructor
  public static class Login {

    @NotBlank(message = "아이디(이름)는 필수입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
  }
}