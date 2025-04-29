package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record ReadStatusRequest
    (@NotBlank(message = "채널 ID는 필수입니다.")
     UUID channelId,
     @NotBlank(message = "유저 ID는 필수입니다.")
     UUID userId
    ) {

}