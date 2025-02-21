package com.sprint.mission.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageDtoForCreate {

    private UUID channelId;
    private UUID userId;
    private String content;
}
