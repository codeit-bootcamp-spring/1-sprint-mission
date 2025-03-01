package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ChannelUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID channelId;
    private final UUID userId;
}
