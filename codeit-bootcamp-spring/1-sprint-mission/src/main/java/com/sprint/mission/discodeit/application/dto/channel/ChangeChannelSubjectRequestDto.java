package com.sprint.mission.discodeit.application.dto.channel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ChangeChannelSubjectRequestDto(
        @NotNull
        UUID channelId,
        @Size(max = 1024, message = "채널 주제 길이는 1024이하 제한입니다.")
        String subject
) {
}
