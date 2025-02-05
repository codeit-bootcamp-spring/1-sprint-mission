package com.sprint.mission.discodeit.entity.user.entity;


import com.google.common.base.Preconditions;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.BaseEntity;
import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.Nickname;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class User extends BaseEntity {

    private Nickname name;
    private Email email;
    private final ParticipatedChannel participatedChannels;

    private User(Nickname name, ParticipatedChannel channel) {
        this.name = name;
        this.participatedChannels = channel;
    }

    public static User createFrom(String username) {
        var userName = new Nickname(username);
        var participatedChannel = ParticipatedChannel.newDefault();
        return new User(userName, participatedChannel);
    }

    public void changeUserName(String newName) {
        this.name = new Nickname(newName);
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

    public String getNicknameValue() {
        return name.getValue();
    }

}

