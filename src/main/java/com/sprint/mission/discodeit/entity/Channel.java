package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "channels")
@Getter
@Builder
public class Channel extends BaseEntity{
    public enum ChannelType {Private, Public};

    @Column(name = "name")
    private String name;

    @Column(name = "discription")
    private String description;
//    private List<UUID> member;
//    private UUID owner;

    @Column(name = "type")
    private ChannelType channelType;

    protected Channel() { }

//List<UUID> member, UUID owner,
    public Channel(String name, String description, ChannelType channelType){
        super();
        this.name = name;
        this.description = description;
//        this.member = member;
//        this.owner = owner;
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
