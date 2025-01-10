package com.sprint.mission.discodeit.entity;

import java.util.List;

public class Channel extends BaseEntity{
    private String name;
    private User channelOwner;
    private final List<User> channelUserList;

    public Channel(String name, User channelOwner, List<User> channelUserList) {
        super();
        this.name = name;
        this.channelOwner = channelOwner;
        this.channelUserList = channelUserList;
    }

    public User getChannelOwner() {
        return channelOwner;
    }

    public void updateChannelOwner(User channelOwner) {
        this.channelOwner = channelOwner;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public List<User> getChannelUserList() {
        return channelUserList;
    }

    public void addChannelUser(User user){
        this.channelUserList.add(user);
    }

    public void deleteChannelUser(User user){
        this.channelUserList.remove(user);
    }
}
