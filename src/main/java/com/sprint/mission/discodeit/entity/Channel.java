package com.sprint.mission.discodeit.entity;

import java.util.List;

public class Channel extends BaseEntity {
    private String name;
    private List<User> members;

    public Channel(String name) {
        super();
        if (name == null){
            throw new NullPointerException("올바른 채널 명을 입력해 주세요.");
        }
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
        if (name == null){
            throw new NullPointerException("올바른 채널 명을 입력해 주세요.");
        }
        this.name = name;
        update();
    }

    public List<User> getMember(String name){
        return members;
    }

}
