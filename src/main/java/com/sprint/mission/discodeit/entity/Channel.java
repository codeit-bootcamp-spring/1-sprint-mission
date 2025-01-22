package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private User owner;
    private String channelName;
    public Channel(User owner, String channelName){
        if(owner == null){
            throw new IllegalArgumentException("User 은 null 일 수 없습니다.");
        }
        if(channelName == null || channelName.trim().isEmpty()){
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없습니다.");
        }
        this.owner = owner;
        this.channelName = channelName;
    }
    public String getChannelName(){
        return channelName;
    }
    public void setChannelName(String channelName){
        if(channelName == null || channelName.trim().isEmpty()){
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없습니다.");
        }
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
