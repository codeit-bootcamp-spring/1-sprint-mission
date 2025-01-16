package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private User user;

    private Channel channel;

    private String content;

    public Message(User user, String content, Channel channel){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.user = user;
        this.content = content;
        this.channel = channel;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public void updateId(UUID id) {
        this.id = id;
    }
    public void updateCreatedAt(Long CreatedAt) {
        this.createdAt = createdAt;
    }

    public void updateMessage(String message){
        this.content = message;
        this.updatedAt = System.currentTimeMillis();
    }
    public Channel getChannel() {
        return channel;
    }

    // 메세지 넣기

    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String name = user.getName();
        return name + "/ createdAt : " + simpleDateFormat.format(createdAt) + "\n" + "[ " + content
                +" ]";

    }
}
