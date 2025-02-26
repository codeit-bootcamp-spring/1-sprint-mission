package com.sprint.mission.discodeit.dto.message;

import lombok.Getter;


@Getter
public class UpdateMessageRequestDto {
    private String newContent;

    public UpdateMessageRequestDto(String newContent) {
        this.newContent = newContent;
    }
}
