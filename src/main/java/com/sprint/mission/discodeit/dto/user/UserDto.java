package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.status.AccountStatus;
import java.util.UUID;

public record UserDto(
    //객체 식별용 id
    UUID id,
    //아이디
    String username,
    //닉네임
    String nickname,
    //이메일 - 로그인용 계정 아이디
    String email,
    //접속 상태
    boolean online,
    //사용자 설정 상태 메세지
    String statusMessage,
    //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
    AccountStatus accountStatus,
    //사용자 프로필 사진
    BinaryContentDto profileId
) {

}
