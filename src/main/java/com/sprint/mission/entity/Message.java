package com.sprint.mission.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private User name;
    private Channel channel;
    private String message;
    private Long createdAt;
    private Long updateAt;


    public Message(User name, Channel channel, String message) {
        {
            this.name = name;
            this.channel = channel;
            this.message = message;
            this.createdAt = System.currentTimeMillis() ;
            this.updateAt = this.createdAt;
        }

    }

    public Channel getChannel () {
            return channel;
    }

    public User getName () {
            return name;
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
    public String toString() {
        return  channel +
                "\n메시지 : " + message;
    }
}

