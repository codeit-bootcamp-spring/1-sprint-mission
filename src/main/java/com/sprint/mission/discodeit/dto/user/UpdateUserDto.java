package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.status.AccountStatus;

import java.time.Instant;

public record UpdateUserDto(
    //username
    String newUsername,
    //닉네임
    String newNickname,
    //비밀번호
    String newPassword,
    //이메일
    String newEmail,
    //접속 상태
    boolean isOnline,
    //사용자 설정 상태 메세지
    String newStatusMessage,
    //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
    AccountStatus accountStatus,
    //갱신 일자
    Instant updatedAt

) {

}







