package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity{
    private User user;
    private String channelName;
    public Channel(User user, String channelName){
        this.user = user;
        this.channelName = channelName;
    }
    public String getChannelName(){
        return channelName;
    }
    public void setChannelName(String channelName){
       this.channelName = channelName;
    }
    public User getUser(){
        return user;
    }
}
