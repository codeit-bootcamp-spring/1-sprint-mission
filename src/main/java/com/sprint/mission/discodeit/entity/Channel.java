package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity{
    private User owner;
    private String channelName;
    public Channel(User owner, String channelName){
        this.owner = owner;
        this.channelName = channelName;
    }
    public String getChannelName(){
        return channelName;
    }
    public void setChannelName(String channelName){
       this.channelName = channelName;
    }
    public User getUser(){return owner;}

    @Override
    public String toString(){
        return "\n"
                + "User : "  + owner.getNickname()
                + "\n"
                + "Channel : " + getChannelName()
                + "\n"
                + "create at : " + getCreatedAt()
                + "\n"
                + "Updated at : " + getUpdatedAt()
                + "\n";
    }
}
