package com.sprint.mission.discodeit.dto.user;

public record CreateUserDto(
        String username,
        //닉네임
        String nickname,
        //이메일 - 로그인용 계정 아이디
        String email,
        //비밀번호
        String password,
        //사용자 설정 상태 메세지
        String statusMessage
) {
}
