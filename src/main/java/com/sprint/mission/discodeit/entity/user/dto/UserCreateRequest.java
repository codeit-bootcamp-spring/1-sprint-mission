package com.sprint.mission.discodeit.entity.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

  @NotEmpty
  private String username;

  @NotEmpty
  @Email
  private String email;

  @NotEmpty
  private String password;

}
