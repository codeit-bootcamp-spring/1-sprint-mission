package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 생성 요청 정보")
public record UserCreateRequest(
    @Schema(description = "사용자 이름", example = "john_doe")
    String username,

    @Schema(description = "이메일", example = "john@example.com")
    String email,

    @Schema(description = "비밀번호", example = "securepassword123")
    String password
) {

}
