package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.status.AccountStatus;

import java.time.Instant;

public record UpdateUserDto(
        //닉네임
        String nickname,
        //이메일
        String email,
        //접속 상태
        boolean isOnline,
        //사용자 설정 상태 메세지
        String statusMessage,
        //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
        AccountStatus accountStatus,
        //사용자 프로필 사진
        String profileImageId,

        Instant updatedAt

) {
}







