package com.sprint.mission.discodeit.dto.channel;

import static com.sprint.mission.discodeit.constant.ValidationConstants.CHANNEL_NAME_MAX_LENGTH;
import static com.sprint.mission.discodeit.constant.ValidationConstants.CHANNEL_NAME_MIN_LENGTH;
import static com.sprint.mission.discodeit.constant.ValidationConstants.MAXIMUM_DESCRIPTION_LENGTH;
import static com.sprint.mission.discodeit.constant.ValidationConstants.MINIMUM_DESCRIPTION_LENGTH;

import jakarta.validation.constraints.Size;

public record ChannelUpdateDto(

    @Size(
        min = CHANNEL_NAME_MIN_LENGTH,
        max = CHANNEL_NAME_MAX_LENGTH,
        message = "channel name must be between : " + CHANNEL_NAME_MIN_LENGTH + " ~ "
            + CHANNEL_NAME_MAX_LENGTH
    )
    String newName,

    @Size(
        min = MINIMUM_DESCRIPTION_LENGTH,
        max = MAXIMUM_DESCRIPTION_LENGTH,
        message = "channel description must be between : " + MINIMUM_DESCRIPTION_LENGTH + " ~ "
            + MAXIMUM_DESCRIPTION_LENGTH
    )
    String newDescription
) {

}
