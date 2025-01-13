package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Channel extends AbstractUUIDEntity {

    @NotNull
    @Size(
            min = 3, max = 50,
            message = "create channel must be between {min} and {max} : reject channel name `${validatedValue}`"
    )
    private String channelName;

    private Channel(String channelName) {
        this.channelName = channelName;
    }

    public static Channel createFrom(String channelName) {
        return new Channel(channelName);
    }

    public String getChannelName() {
        return channelName;
    }

    public void ChangeName(String newName) {
        channelName = newName;
    }

    public boolean isEqualFromName(String channelName) {
        return this.channelName.equals(channelName);
    }

}
