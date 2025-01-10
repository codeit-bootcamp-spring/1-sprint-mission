package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.UtilMethod;

import java.util.UUID;

public class Message extends BaseEntity{
    private final User sendUser;
    private final Channel channel;
    private String content;

    public Message(User sendUser, Channel channel, String content) {
        super();
        this.sendUser = sendUser;
        this.channel = channel;
        this.content = content;
    }

    public User getSendUser() {
        return sendUser;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
