package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.common.error.ErrorMessage;
import com.sprint.mission.discodeit.common.error.channel.ChannelException;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.entity.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Channel extends AbstractUUIDEntity {

    @NotNull
    @Size(
            min = 3, max = 50,
            message = "create channel must be between {min} and {max} : reject channel name `${validatedValue}`"
    )
    private String channelName;

    private final User creator; // 채널의 주인이 바뀌기도 하나..? 안돼 돌아가

    private Channel(String channelName, User creator) {
        this.channelName = channelName;
        this.creator = creator;
    }

    public static Channel createOfChannelNameAndUser(
            String channelName,
            User creator
    ) {

        return new Channel(channelName, creator);
    }

    public static Channel createDefaultNameAndUser(User user) {

        return new Channel("Default Channel Name", user);
    }

    public void changeName(String newName, User user) {
        if (!isCreator(user)) {
            throw ChannelException.ofErrorMessageAndCreatorName(
                    ErrorMessage.CHANNEL_NOT_EQUAL_CREATOR,
                    user.getName()
            );
        }

        channelName = newName;
        updateStatusAndUpdateAt();
    }

    public void deleteChannel(User user) {
        if (!isCreator(user)) {
            throw ChannelException.ofErrorMessageAndCreatorName(
                    ErrorMessage.CHANNEL_NOT_EQUAL_CREATOR,
                    user.getName()
            );
        }

        updateUnregistered();
    }

    public boolean isStatusNotUnregisteredAndEqualsTo(String channelName) {
        return isNotUnregistered() && this.channelName.equals(channelName);
    }

    public String getChannelName() {
        return channelName;
    }

    private boolean isCreator(User user) {
        return this.creator.equals(user);
    }

}
