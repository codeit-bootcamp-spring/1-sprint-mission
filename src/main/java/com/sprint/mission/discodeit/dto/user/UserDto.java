package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.util.UUID;

public record UserDto(
    //객체 식별용 id
    UUID id,
    //아이디
    String username,
    //이메일 - 로그인용 계정 아이디
    String email,
    //접속 상태
    boolean online,
    //사용자 프로필 사진
    BinaryContentDto profile
) {

}
