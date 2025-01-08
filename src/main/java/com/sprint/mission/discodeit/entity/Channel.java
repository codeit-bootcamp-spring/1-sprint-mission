package com.sprint.mission.discodeit.entity;

import java.util.List;

public class Channel extends BaseEntity {
    private String name;
    private List<User> members;

    public Channel(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        members.add(user);
        update();
    }

    public void removeMember(User user) {
        members.remove(user);
        update();
    }

    public void update(String name) {
        this.name = name;
        update();
    }

}
