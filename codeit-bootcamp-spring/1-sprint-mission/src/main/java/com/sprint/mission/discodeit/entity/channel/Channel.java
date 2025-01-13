package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.entity.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Channel extends AbstractUUIDEntity {

    @NotNull
    @Size(
            min = 3, max = 50,
            message = "create channel must be between {min} and {max} : reject channel name `${validatedValue}`"
    )
    private String channelName;

    private final User creator;

    private Channel(String channelName, User creator) {
        this.channelName = channelName;
        this.creator = creator;
    }

    public static Channel createFromChannelNameAndUser(String channelName, User creator) {
        return new Channel(channelName, creator);
    }

    public static Channel createDefaultNameAndUser(User user) {
        return new Channel("Default Channel Name", user);
    }

    public void changeName(String newName) {
        channelName = newName;
        updateStatusAndUpdateAt();
    }

    public void deleteChannel() {
        updateUnregistered();
    }

    public boolean isEqualFromNameAndNotUnregistered(String channelName) {
        return this.channelName.equals(channelName) && isNotUnregistered();
    }

    public String getChannelName() {
        return channelName;
    }

}
