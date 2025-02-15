package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MessageDtoForUpdate {
    private UUID messageId;
    private String changeContent;
}
