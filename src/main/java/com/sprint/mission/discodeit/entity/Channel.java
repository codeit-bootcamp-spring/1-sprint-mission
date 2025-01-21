package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Channel extends BaseEntity{
    private String channelName;
    private String description;
    private List<User> member;
    private User owner;

    public Channel(String channelName, String description, List<User> member, User owner){
        super();
        this.channelName = channelName;
        this.description = description;
        this.member = member;
        this.owner = owner;
    }
}
