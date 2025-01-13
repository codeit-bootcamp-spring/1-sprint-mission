package com.sprint.mission.discodeit.entity.user.entity;


import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import java.util.List;
import java.util.UUID;

public class User extends AbstractUUIDEntity {

    private UserName name;

    private final ParticipatedChannel channels;

    public User(UserName name, ParticipatedChannel channel) {
        this.name = name;
        this.channels = channel;
    }

    public static User createFrom(final String username) {
        var userName = UserName.createFrom(username);
        var participatedChannel = ParticipatedChannel.newDefault();
        return new User(userName, participatedChannel);
    }

    public void changeUserName(String newName) {
        this.name = name.changeName(newName);
        updateStatusAndUpdateAt();
    }

    public Channel createNewChannel(String channelName) {
        var createdChannel = channels.createChannel(channelName, this);
        return createdChannel;
    }

    public void changeChannelName(UUID id, String channelName) {
        channels.changeChannelNameOrThrow(id, channelName, this);
    }

    public List<Channel> getChannels() {
        return channels.findAllChannels();
    }

    public String getName() {
        return name.getName();
    }

    public void unregister() {
        updateUnregistered();
    }
}

