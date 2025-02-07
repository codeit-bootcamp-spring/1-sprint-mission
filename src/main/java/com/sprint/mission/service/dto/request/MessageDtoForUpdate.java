package com.sprint.mission.service.dto.request;

import com.sprint.mission.entity.BinaryMessageContent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageDtoForUpdate {
    private UUID messageId;
    private String changeContent;
    private BinaryMessageContent binaryMessageContent;
}
