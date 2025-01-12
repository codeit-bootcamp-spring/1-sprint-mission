package com.sprint.mission.entity;

import java.util.UUID;

public class Message {
    private User user;
    private Channel channel;
    private String message;
    private long createdAt;


    public Message(User user, Channel channel, String message) {
        {
            this.user = user;
            this.channel = channel;
            this.message = message;
            this.createdAt = System.currentTimeMillis();
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
    }

    public long getCreatedAt () {
            return createdAt;
    }

    @Override
    public String toString () {
        return "Message{" +
                    ", user=" + user +
                    ", channel='" + channel + '\'' +
                    ", message='" + message + '\'' +
                    ", createdAt=" + createdAt +
                    '}';
    }
}

