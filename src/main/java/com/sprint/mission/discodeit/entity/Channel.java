package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.channel.ChannelOwnerNotNullException;
import com.sprint.mission.discodeit.global.error.execption.user.UserNotNullException;
import com.sprint.mission.discodeit.global.util.TimeUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity {

    private String name;
    private String description;
    private User channelOwner;
    private final List<User> channelUserList = new ArrayList<>();
    private final ChannelType channelType;

    private Channel(String name, String description, User channelOwner, ChannelType channelType) {
        super();
        this.name = name;
        this.description = description;
        this.channelOwner = channelOwner;
        this.channelType = channelType;
    }

    public static Channel of(String name, String description, User channelOwner, ChannelType channelType) {
        if (channelOwner == null) {
            throw new ChannelOwnerNotNullException();
        }
        return new Channel(name, description, channelOwner, channelType);
    }

    public void updateChannelOwner(User channelOwner) {
        if (channelOwner == null) {
            throw new ChannelOwnerNotNullException();
        }
        this.channelOwner = channelOwner;
        this.updateUpdatedAt(TimeUtil.getCurrentTime());
    }

    public void updateName(String name) {
        this.name = name;
        this.updateUpdatedAt(TimeUtil.getCurrentTime());
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updateUpdatedAt(TimeUtil.getCurrentTime());
    }

    public void updateChannelInfo(String name, String description) {
        this.updateName(name);
        this.updateDescription(description);
    }

    public void addChannelUser(User user) {
        if (user == null) {
            throw new UserNotNullException();
        }
        this.channelUserList.add(user);
    }

    public void removeUserFromChannel(User user) {
        if (user == null) {
            throw new UserNotNullException();
        }
        this.channelUserList.remove(user);
    }

    public boolean isNotOwner(UUID channelOwnerId) {
        return !(this.channelOwner.getId().equals(channelOwnerId));
    }

    public boolean isPrivate() {
        return this.channelType == ChannelType.PRIVATE;
    }

    public boolean isPublic() {
        return this.channelType == ChannelType.PUBLIC;
    }

    public boolean isUserInChannel(User user) {
        return (this.channelUserList.contains(user) || channelOwner.equals(user));
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", channelOwner=" + channelOwner +
                ", channelUserList=" + channelUserList +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Channel channel = (Channel) object;
        return Objects.equals(name, channel.name)
                && Objects.equals(description, channel.description)
                && Objects.equals(channelOwner, channel.channelOwner)
                && Objects.equals(channelUserList, channel.channelUserList) && channelType == channel.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, channelOwner, channelUserList, channelType);
    }
}
