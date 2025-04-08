package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

  @NotBlank(message = "사용자명은 필수입니다.")
  @Size(min = 2, max = 20, message = "사용자명은 2자 이상 20자 이하여야 합니다.")
  public String username;

  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  public String email;

  @NotBlank(message = "비밀번호는 필수입니다.")
  public String password;
}
