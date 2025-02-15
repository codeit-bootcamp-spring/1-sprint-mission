package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class MessageDtoForCreate {

    private UUID channelId;
    private UUID userId;
    private String content;
    private List<byte[]> attachments;

}
