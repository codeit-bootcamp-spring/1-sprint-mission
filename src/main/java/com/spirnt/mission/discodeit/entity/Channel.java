package com.spirnt.mission.discodeit.entity;

import java.util.List;

public class Channel extends BaseEntity{
    private String channelName;
    private String description;
    private List<User> member;
    private User owner;



    public Channel(String channelName, String description, List<User> member, User owner){
        super();
        setChannelName(channelName);
        setDescription(description);
        setMember(member);
        setOwner(owner);

    }

    public void setDescription(String description) { this.description = description; }
    public void setChannelName(String channelName) { this.channelName = channelName; }
    public void setMember(List<User> member){ this.member = member; }
    public void setOwner(User owner){ this.owner = owner; }

    public String getChannelName() { return channelName; }
    public String getDescription() { return description; }
    public List<User> getMember() { return member; }
    public User getOwner() { return owner; }

}
