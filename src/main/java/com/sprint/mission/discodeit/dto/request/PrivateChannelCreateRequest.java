package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    @NotNull(message = "참여자 목록은 필수입니다")
    @NotEmpty(message = "참여자 목록은 비어있을 수 없습니다")
    @Size(min = 2, message = "비공개 채널에는 최소 2명의 참여자가 필요합니다")
    List<UUID> participantIds
) {

}
