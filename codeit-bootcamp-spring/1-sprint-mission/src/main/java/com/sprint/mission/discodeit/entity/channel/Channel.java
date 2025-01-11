package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Channel extends AbstractUUIDEntity {

    @NotNull
    @Size(min = 3, max = 50, message = "create channel must be between {min} and {max} : reject channel name `${validatedValue}`")
    private String channelName;

    private Channel(String channelName) {
        this.channelName = channelName;
    }

    public static Channel of(String channelName) {
        return new Channel(channelName);
    }

    public String getChannelName() {
        return channelName;
    }

}
