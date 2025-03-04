package com.sprint.mission.discodeit.dto.login;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 정보")
public record LoginRequest(
    String username,
    String password
) {

}
