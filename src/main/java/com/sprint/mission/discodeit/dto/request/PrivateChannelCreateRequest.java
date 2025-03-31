package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    @NotNull(message = "Participant IDs cannot be null") // 목록별 null 검사
    @NotEmpty(message = "Participant IDs cannot be empty") // 컬렉션이 비어있는지 검사
    List<UUID> participantIds
) {

}
