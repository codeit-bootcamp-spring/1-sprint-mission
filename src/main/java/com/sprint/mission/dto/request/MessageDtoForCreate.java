package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.Message;
import lombok.Getter;

import java.util.UUID;

public record MessageDtoForCreate(
    UUID channelId,
    UUID userId,
    String content) {

    public Message toEntity() {
        return new Message(
            channelId,
            userId,
            content
        );
    }
}
