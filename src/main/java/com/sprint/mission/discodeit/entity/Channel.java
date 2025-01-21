package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Channel extends BaseEntity{
    private String name;
    private String description;
    private List<User> member;
    private User owner;

    public Channel(String name, String description, List<User> member, User owner){
        super();
        this.name = name;
        this.description = description;
        this.member = member;
        this.owner = owner;
    }
}
