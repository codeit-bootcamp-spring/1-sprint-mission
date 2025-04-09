package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PrivateChannelCreateRequest {
    List<UUID> participantIds;
}