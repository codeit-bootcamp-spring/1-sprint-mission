package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChannelUpdateRequestDto {

    @Size(min = 1, max = 20, message = "채널 이름은 한 글자 이상 20자 이하")
    private String newName;
    private String NewDescription;

    public ChannelUpdateRequestDto(String newName, String newDescription) {
        this.newName = newName;
        NewDescription = newDescription;
    }
}
