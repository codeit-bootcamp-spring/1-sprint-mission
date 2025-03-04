package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 정보")
public record LoginRequest(
    @Schema(description = "사용자 이름", example = "john")
    String username,

    @Schema(description = "비밀번호", example = "password123")
    String password
) {

}