package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends BaseEntity {
    private String name;
    private String description;
    private boolean isPrivate;
    private List<User> members;

    public Channel(String name, String description, boolean isPrivate, List<User> members) {
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
        this.members = members;
    }

    //getter
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public boolean isPrivate() {
        return isPrivate;
    }
    public List<User> getMembers() {
        return members;
    }
    // update
    public void updateName(String Name) {
        this.name = Name;
        update();
    }
    public void updateDescription(String Description) {
        this.description = Description;
        update();
    }
    public void updateIsPrivate(boolean IsPrivate) {
        this.isPrivate = IsPrivate;
        update();
    }

    //member 메소드 구현
    public void addMember (User member) {
        if (!members.contains(member)) {
            members.add(member);
            update();
        }
    }
    public void removeMember (User member) {
        if(members.contains(member)) {
            members.remove(member);
            update();
        }
    }


}
