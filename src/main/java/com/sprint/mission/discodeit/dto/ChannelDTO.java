package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO {
    private String name;
    private String description;
    private String type;

}