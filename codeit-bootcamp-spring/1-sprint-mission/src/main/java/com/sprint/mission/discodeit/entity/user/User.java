package com.sprint.mission.discodeit.entity.user;


import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.entity.user.entity.ParticipatedChannel;
import java.util.ArrayList;

public class User extends AbstractUUIDEntity {

    private UserName name;

    private final ParticipatedChannel channel;

    public User(UserName name) {
        this.name = name;
        // TODO of로 생성했을 때 불변객체라는건 아는데, of로 초기화했을때의 문제점 없는가? static factory method 안에서 초기화 하는 방법은?
        channel = ParticipatedChannel.from(new ArrayList<Channel>(1_000));
    }

    public static User from(final String username) {
        var userName = UserName.from(username);
        return new User(userName);
    }

    public void changeName(String newName) {
        this.name = name.changeName(newName);
        updateStatusAndUpdateAt();
    }

    public Channel createChannel(String channelName) {
        var createdChannel = channel.createChannel(channelName);
        return createdChannel;
    }

    public String getName() {
        return name.getName();
    }

    public void unregister() {
        updateUnregistered();
    }
}

