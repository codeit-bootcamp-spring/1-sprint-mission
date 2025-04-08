package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
    @NotBlank
    @Size(min = 1, max = 50)
    String username,
    //닉네임
    @Size(max = 50)
    String nickname,
    @NotBlank
    @Email
    //이메일 - 로그인용 계정 아이디
    String email,
    //비밀번호
    @NotBlank
    @Size(min = 1, max = 50)
    String password,
    //사용자 설정 상태 메세지
    @Size(min = 1, max = 50)
    String statusMessage
) {

}
