package com.sprint.mission.discodeit.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class LoginRequest {

  @Email(message = "올바른 이메일 형식이어야 합니다")
  @NotBlank(message = "이메일은 필수입니다")
  @JsonProperty("username")
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다")
  private String password;

  @JsonCreator
  public LoginRequest(
      @JsonProperty(value = "username", required = true) String email,
      @JsonProperty(value = "password", required = true) String password) {
    this.email = email;
    this.password = password;
  }
}