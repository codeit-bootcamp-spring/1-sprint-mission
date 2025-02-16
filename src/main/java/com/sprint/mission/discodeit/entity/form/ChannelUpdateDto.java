package com.sprint.mission.discodeit.entity.form;

import com.sprint.mission.discodeit.entity.BaseEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ChannelUpdateDto extends BaseEntity {
    @NotEmpty
    private String channelName;

    private String description;
}
