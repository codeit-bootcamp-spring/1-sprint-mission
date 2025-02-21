package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreatePrivateChannelDto(
    List<String> participantIds
){
}
