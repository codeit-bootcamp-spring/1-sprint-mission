package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.domain.ChannelType;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private List<UUID> member;
    private UUID owner;
    private ChannelType channelType;

    public Channel(String name, String description, List<UUID> member, UUID owner, ChannelType channelType){
        super();
        this.name = name;
        this.description = description;
        this.member = member;
        this.owner = owner;
        this.channelType = channelType;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public boolean isPrivate(){
        return this.channelType == ChannelType.Private;
    }
}
