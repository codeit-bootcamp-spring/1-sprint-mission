package com.spirnt.mission.discodeit.dto.channel;

import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ChannelResponse {
    private UUID channelId;
    private String name;
    private String description;
    private ChannelType type;
    private List<UUID> usersId;
    private Instant lastMessageAt;

    public ChannelResponse(Channel channel, List<UUID> usersId, Instant lastMessageAt) {
        this.name = channel.getName();
        this.description = channel.getDescription();
        this.channelId = channel.getId();
        this.type = channel.getType();
        this.usersId = (usersId==null)? new ArrayList<>():usersId;
        this.lastMessageAt = lastMessageAt;
    }

    @Override
    public String toString() {
        if(this.type==ChannelType.PUBLIC) {
            return "PUBLIC Channel[Name: " + this.getName() +
                    " Description: " + this.getDescription() +
                    " ID: " + this.channelId +" ]";
        } else if(this.type==ChannelType.PRIVATE) {
            return "PRIVATE Channel[" +
                    " ID: " + this.channelId + "]"+
                    "\nParticipants: "+usersId;
        }
        else return "";
    }
}
