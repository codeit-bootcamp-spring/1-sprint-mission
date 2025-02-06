package com.sprint.mission.discodeit.dto.user;

public record CreateUserDto(
        //닉네임
        String nickname,
        //이메일 - 로그인용 계정 아이디
        String email,
        //사용자 설정 상태 메세지
        String statusMessage,
        //프로필 이미지
        String profileImageId
) {
}
