package com.sprint.mission.discodeit.entity;

import java.util.Collections;
import java.util.List;

public class VoiceBehavior implements ChannelBehavior {

    List<User> participants;
    @Override
    public void setChannelPrivate(Channel channel) {
        channel.updatePrivate(false, channel);
    }

    @Override
    public void setChannelPublic(Channel channel) {
        channel.updatePrivate(true, channel);
    }

    public void addParticipant(User user){
        participants.add(user);
    }

    public void removeParticipant(User user){
        participants.remove(user);
    }

    public List<User> getParticipants(){
        return Collections.unmodifiableList(participants);
    }
}
