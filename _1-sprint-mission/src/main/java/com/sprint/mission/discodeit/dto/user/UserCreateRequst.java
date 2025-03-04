package com.sprint.mission.discodeit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

//요청
@Schema(description = "User 생성 정보")
public record UserCreateRequst(
    String username,
    String email,
    String password,
    String phoneNumber,
    String address
) {

}
