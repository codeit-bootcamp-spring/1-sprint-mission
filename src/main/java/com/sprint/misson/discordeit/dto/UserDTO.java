package com.sprint.misson.discordeit.dto;

import com.sprint.misson.discordeit.entity.AccountStatus;
import com.sprint.misson.discordeit.entity.BinaryContent;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.entity.status.UserStatus;
import lombok.Getter;

public record UserDTO(

        //id
        String id,
        //닉네임
        String nickname,
        //이메일 - 로그인용 계정 아이디
        String email,
        //접속 상태
        Boolean isOnline,
        //사용자 설정 상태 메세지
        String statusMessage,
        //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
        AccountStatus accountStatus,
        //프로필 이미지
        String profileImageId

) {
}
