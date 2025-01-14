package com.sprint.mission.discodeit.entity.user.entity;


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

    public static User createFrom(final String username) {
        var userName = UserName.createFrom(username);
        var participatedChannel = ParticipatedChannel.newDefault();
        return new User(userName, participatedChannel);
    }

    public void changeUserName(String newName) {
        var changedName = this.name.changeName(newName);
        this.name = changedName;
        updateStatusAndUpdateAt();
    }

    /**
     * TODO 채널을 생성하는 책임이 여기? 아님!!
     * 채널 객체 자체를 생성하는 것은 아님 채널 객체에 요청하는 것인데 약간 애매하다.
     * 채널 서비스 레이어에서 호출해서 채널이 생성되면 디비에 저장하는 로직 구상중
     */
    public Channel createNewChannel(String channelName) {
        var createdChannel = participatedChannels.createChannel(channelName, this);
        return createdChannel;
    }

    public Channel changeChannelName(UUID channelId, String channelName) {
        var targetChannel =
                participatedChannels.changeChannelNameOrThrow(channelId, channelName, this);

        return targetChannel;
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

