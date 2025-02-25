package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 수정 요청 정보")
public record UserUpdateRequest(
    @Schema(description = "새로운 사용자 이름", example = "new_john_doe")
    String newUsername,

    @Schema(description = "새로운 이메일", example = "new_john@example.com")
    String newEmail,

    @Schema(description = "새로운 비밀번호", example = "newsecurepassword123")
    String newPassword
) {

}
