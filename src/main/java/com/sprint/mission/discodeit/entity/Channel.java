package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channelName;
    private String description;
    private final User creator;

    public Channel(String channelName, String description, User creator) {
        super();
        this.channelName = channelName;
        this.description = description;
        this.creator = creator;
    }

    public User getCreator() {
        return creator;
    }

    public String getChannel() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public void update(String channelName, String description) {
        boolean flag=false;
        if(channelName!=null&&!channelName.equals(this.channelName)){
            this.channelName = channelName;
            flag=true;
        }
        if(description!=null&&!description.equals(this.description)){
            this.description = description;
            flag=true;
        }
        if(flag){
            update();
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channel='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                '}';
    }
}
