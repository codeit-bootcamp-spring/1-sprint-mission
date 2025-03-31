package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.status.AccountStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.hibernate.validator.constraints.Length;

public record UpdateUserDto(

    @Size(min = 1, max = 20)
    //username
    String newUsername,
    //닉네임
    @Size(min = 1, max = 20)
    String newNickname,
    //비밀번호
    @Size(min = 1, max = 20)
    String newPassword,
    //이메일
    @Email
    String newEmail,
    //접속 상태
    boolean isOnline,
    //사용자 설정 상태 메세지
    @Size(max = 50)
    String newStatusMessage,
    //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
    AccountStatus accountStatus,
    //갱신 일자
    @NotNull
    Instant updatedAt

) {

}







