package com.sprint.mission.discodeit.dto.channel;

import static com.sprint.mission.discodeit.constant.ValidationConstants.MINIMUM_PRIVATE_CHANNEL_PARTICIPANTS;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreatePrivateChannelDto(
    @NotEmpty(message = "participants cannot be null/empty")
    @Size(min = MINIMUM_PRIVATE_CHANNEL_PARTICIPANTS, message = "there must be at least two participants to create a channel")
    List<String> participantIds
) {

}
