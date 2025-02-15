package com.sprint.mission.discodeit.dto.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class MessageBaseDTO {
    private String content;
    public MessageBaseDTO(String content) {
        this.content = content;
    }
}
