package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateDTO(
//        @NotEmpty(message = "참여자 ID는 필수입니다.")
        List<UUID> participantIds) {
}
