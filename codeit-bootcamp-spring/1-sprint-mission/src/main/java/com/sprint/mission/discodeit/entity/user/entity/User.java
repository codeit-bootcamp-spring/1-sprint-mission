package com.sprint.mission.discodeit.entity.user.entity;


import com.google.common.base.Preconditions;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import java.util.List;
import java.util.UUID;

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

    /**
     *  유저에게 채널을 생성하는 책임이 있는 것이아니라,
     *  다른 객체에 해당 작업을 위임, 그런데 메서드 명이 영...
     *  이 부분 코드 리뷰 부탁드릴게요!
     */
    public Channel createNewChannel(String channelName) {
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

    public void deleteParticipatedChannel(UUID channelId) {
        participatedChannels.deleteChannelById(channelId, this);
    }

    public List<Channel> getParticipatedChannels() {
        return participatedChannels.findAllChannels();
    }

    public String getName() {
        return name.getName();
    }

    public void unregister() {
        updateUnregistered();
    }
}

