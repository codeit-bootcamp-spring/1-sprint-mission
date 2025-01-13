package com.sprint.mission.entity;

import java.util.UUID;

public class Message {
    private User user;
    private Channel channel;
    private String message;
    private long createdAt;
    private long updateAt;


    public Message(User user, Channel channel, String message) {
        {
            this.user = user;
            this.channel = channel;
            this.message = message;
            this.createdAt = System.currentTimeMillis();
            this.updateAt = this.createdAt;
        }

    }

    public Channel getChannel () {
            return channel;
    }

    public User getUser () {
            return user;
    }

    public String getMessage () {
            return message;
    }

    public void setMessage (String message){
            this.message = message;
            this.updateAt = System.currentTimeMillis();
    }

    public long getCreatedAt () {
            return createdAt;
    }

    @Override
    public String toString () {
        return "\n유저 : " + user + channel  + "\n메시지 : " + message +
                    ", 생성 : " + createdAt;
    }
}

