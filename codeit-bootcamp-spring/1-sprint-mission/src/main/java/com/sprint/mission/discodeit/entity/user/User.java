package com.sprint.mission.discodeit.entity.user;


import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.entity.user.db.ParticipatedChannel;

public class User extends AbstractUUIDEntity {

    private UserName name;

    private final ParticipatedChannel channel;

    public User(UserName name) {
        this.name = name;
        channel = ParticipatedChannel.of();
    }

    public String getName() {
        return name.getName();
    }

    public void changeName(String newName) {
        this.name = name.changeName(newName);
        updateStatusAndUpdateAt();
    }

    public void unregister() {
        updateUnregistered();
    }

    public Channel createChannel(String channelName) {
        var channel = this.channel.createChannel(channelName);
        return channel;
    }
}

