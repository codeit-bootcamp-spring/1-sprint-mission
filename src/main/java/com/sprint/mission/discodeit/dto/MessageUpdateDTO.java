package com.sprint.mission.discodeit.dto;

import lombok.Getter;

@Getter
public class MessageUpdateDTO {
    private String content;

    public MessageUpdateDTO(String content) {
        this.content = content;
    }
}
