package com.sprint.mission.discodeit.dto.form;

import com.sprint.mission.discodeit.dto.entity.BaseEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelUpdateDto extends BaseEntity {
    @NotEmpty
    private String channelName;

    private String description;

}
