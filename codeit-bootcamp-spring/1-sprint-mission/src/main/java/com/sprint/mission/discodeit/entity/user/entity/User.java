package com.sprint.mission.discodeit.entity.user.entity;


import com.google.common.base.Preconditions;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends AbstractUUIDEntity {

    private UserName name;

    private final ParticipatedChannel participatedChannels;

    private User(UserName name, ParticipatedChannel channel) {
        this.name = name;
        this.participatedChannels = channel;
    }

    public static User createFrom(String username) {
        Preconditions.checkNotNull(username);
        var userName = UserName.createFrom(username);
        var participatedChannel = ParticipatedChannel.newDefault();
        return new User(userName, participatedChannel);
    }

    public void changeUserName(String newName) {
        Preconditions.checkNotNull(newName);
        var changedName = this.name.changeName(newName);
        this.name = changedName;
        updateStatusAndUpdateAt();
    }

    public Channel openNewChannel(String channelName) {
        Preconditions.checkNotNull(channelName);
        var createdChannel = participatedChannels.createChannel(channelName, this);
        return createdChannel;
    }

    public Channel changeChannelName(UUID channelId, String channelName) {
        Preconditions.checkNotNull(channelId);
        var targetChannel =
                participatedChannels.changeChannelNameOrThrow(channelId, channelName, this);

        return targetChannel;
    }

    public void exitParticipatedChannel(UUID channelId) {
        participatedChannels.exitChannelById(channelId);
    }

    public int countParticipatedChannels() {
        var participatedChannelCount = participatedChannels.countParticipatedChannels();
        return participatedChannelCount;
    }

    public List<Channel> getParticipatedChannels() {
        return participatedChannels.findAllChannels();
    }

    public void unregister() {
        updateUnregistered();
    }
    public String getUserName() {
        return name.getName();
    }
    @Override
    public String toString() {
        var format =
                String.format(
                        "user info = [id : %s, name: %s, status : %s, createAt = %d, updateAt = %d], participatedChannel = {%s}",
                        getId(),
                        getName(),
                        getStatus().toString(),
                        getCreateAt().toEpochMilli(),
                        getUpdateAt().toEpochMilli(),
                        getParticipatedChannels()
                );

        return format;
    }
}

