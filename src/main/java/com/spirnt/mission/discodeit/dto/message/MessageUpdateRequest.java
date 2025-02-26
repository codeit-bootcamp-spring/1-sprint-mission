package com.spirnt.mission.discodeit.dto.message;

import lombok.Getter;

@Getter
public class MessageUpdateRequest {
    private String content;

    public MessageUpdateRequest(String content) {
        this.content = content;
    }
}