package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record PrivateChannelCreateRequest(
        String channelName,
        UUID adminId
) {}
