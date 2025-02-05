package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreatePrivateChannelDto(

    @NotNull
    Channel.ChannelType channelType,
    @NotBlank
    String serverId,
    @Min(1) @Max(50)
    int maxNumberOfPeople,
    @Size(min = 1, max = 50)
    List<String> userIds

){
}
