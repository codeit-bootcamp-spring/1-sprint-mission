package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.AccountStatus;
import com.sprint.mission.discodeit.entity.User;

public record UserResponseDto(
        //객체 식별용 id
        String id,
        //아이디
        String username,
        //닉네임
        String nickname,
        //이메일 - 로그인용 계정 아이디
        String email,
        //접속 상태
        boolean isOnline,
        //사용자 설정 상태 메세지
        String statusMessage,
        //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
        AccountStatus accountStatus,
        //사용자 프로필 사진
        String profileImageId
) {
    public static UserResponseDto from(User user, boolean isActive) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                isActive,
                user.getStatusMessage(),
                user.getAccountStatus(),
                user.getProfileImageId()
        );
    }

    public void displayInfo() {
        System.out.println(
                "id: " + id +
                        " nickname: " + nickname
                        + " email: " + email
                        + " isOnline: " + isOnline
                        + " statusMessage: " + statusMessage
                        + " accountStatus: " + accountStatus
                        + " profileImageId: " + profileImageId
        );
    }
}
