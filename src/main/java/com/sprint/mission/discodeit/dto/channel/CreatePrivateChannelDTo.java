package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreatePrivateChannelDTo(
    @NotNull
    List<String> participantIds
) {

}
