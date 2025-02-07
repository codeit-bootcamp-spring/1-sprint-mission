package com.sprint.mission.service.dto.request;

import com.sprint.mission.entity.BinaryMessageContent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageDtoForCreate {

    private UUID channelId;
    private UUID userId;
    private String content;
    private BinaryMessageContent binaryMessageContent;

}
