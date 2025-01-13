package com.sprint.mission.discodeit.entity.user;


import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.entity.user.entity.ParticipatedChannel;

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

    public void changeName(String newName) {
        this.name = name.changeName(newName);
        updateStatusAndUpdateAt();
    }

    public Channel createNewChannel(String channelName) {
        var createdChannel = channels.createChannel(channelName);
        return createdChannel;
    }

    public String getName() {
        return name.getName();
    }

    public void unregister() {
        updateUnregistered();
    }
}

