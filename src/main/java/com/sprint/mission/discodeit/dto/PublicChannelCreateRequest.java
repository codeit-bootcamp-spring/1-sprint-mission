package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record PublicChannelCreateRequest(
        String channelName,
        UUID adminId
) {}
