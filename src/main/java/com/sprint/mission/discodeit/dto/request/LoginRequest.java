package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "사용자 이름을 입력하세요")
    @Size(min = 3, max = 50, message = "사용자 이름은 3-50자 사이여야 합니다")
    String username,

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    String password
) {

}