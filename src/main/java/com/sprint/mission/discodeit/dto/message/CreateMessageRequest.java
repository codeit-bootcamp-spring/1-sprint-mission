package com.sprint.mission.discodeit.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@Getter
@AllArgsConstructor
public class CreateMessageRequest {
    private final UUID authorId;
    private final UUID channelId;
    private final String content;
}

