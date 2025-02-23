package com.sprint.mission.discodeit.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Channel implements Serializable {

    private UUID id;
    private Instant createAt;
    private Instant updateAt;
   // @NotEmpty
    private String channelName;
    private String description;
    private ChannelGroup group;
    private Instant lastMessageTime;

    //@OneToMany(mappedBy) 양방향관계 생성
    private List<Participant> participants = new ArrayList<>();

    public Channel(String channelName, String description, ChannelGroup group) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();

        this.channelName = channelName;
        this.description = description;
        this.group = group;
    }
    public Channel(UUID id,String channelName, String description, ChannelGroup group) {
        this.id = id;

        this.channelName = channelName;
        this.description = description;
        this.group = group;
    }

    public Channel(UUID id,ChannelGroup group) {
        this.id = id;
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.group = group;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setChannel(this);
    }

    public boolean isPublic(Channel channel) {
        if(channel.getGroup()==ChannelGroup.PUBLIC) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
