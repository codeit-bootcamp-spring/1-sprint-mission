package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
//import org.hibernate.validator.constraints.Length; -- validator 왜 안되는지 모르겠음

@Getter
@Builder
public class UserCreateRequest {
    @NotBlank(message = "이름은 필수값입니다.")
    @Size(min = 2, message = "이름은 2글자 이상이어야 합니다.")
    private String username;

    @NotBlank(message = "이메일은 필수값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
}