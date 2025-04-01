package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginDTO {

  @NotBlank(message = "아이디는 필수입니다.")
  String username;

  @NotBlank(message = "비밀번호는 필수입니다.")
  String password;
}
