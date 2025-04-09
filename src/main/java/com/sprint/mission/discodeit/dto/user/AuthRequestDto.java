package com.sprint.mission.discodeit.dto.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {

  @NotBlank(message = "사용자 이름은 필수")
  @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하")
  private String username;
  //  @Pattern(
//      regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$",
//      message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
//  )
  private String password;
}
