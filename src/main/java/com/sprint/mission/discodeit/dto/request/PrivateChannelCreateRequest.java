package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    @NotEmpty(message = "참가자 ID 리스트는 비어 있을 수 없습니다.")
    @Size(min = 1, message = "최소 한 명 이상의 참가자가 있어야 합니다.")
    List<UUID> participantIds
) {

}
