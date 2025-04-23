package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.status.AccountStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record UpdateUserDto(

    @Size(max = 20)
    //username
    String newUsername,
    //비밀번호
    @Size(max = 20)
    String newPassword,
    //이메일
    @Email
    String newEmail,
    //접속 상태
    boolean isOnline,
    //사용자 설정 상태 메세지
    @Size(max = 50)
    String newStatusMessage,
    //갱신 일자
    Instant updatedAt
) {

}







