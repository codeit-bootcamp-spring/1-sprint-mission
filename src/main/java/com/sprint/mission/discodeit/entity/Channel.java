package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
