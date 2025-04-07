package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @NotBlank(message = "채널 이름을 입력하세요")
    @Size(min = 3, max = 50, message = "채널 이름은 3-50자 사이여야 합니다")
    String newName,

    @Size(max = 200, message = "채널 설명은 최대 200자까지 입력 가능합니다")
    String newDescription
) {

}