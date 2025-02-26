package com.sprint.mission.discodeit.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelUpdateDto {
    @NotEmpty
    private String newChannelName;

    private String newDescription;

}
