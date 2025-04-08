package com.sprint.mission.discodeit.entity.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

  @NotBlank
  private String username;

  @Email
  @NotBlank
  private String email;

  @Size(min = 6, max = 32)
  @NotBlank
  private String password;

}
