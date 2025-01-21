package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.jcf.JCF_user;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private JCF_user jcfUser = new JCF_user();
    private String userName;
    private UUID userId;

    private UUID channelId;

    private String content;

    public Message(UUID userId, String content, UUID channelId, String userName){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.userId = userId;
        this.content = content;
        this.channelId = channelId;
        this.userName = userName;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isUserEqual(UUID userId) {
        return userId.equals(getUser);
    }

    public boolean isChannelEqual(UUID getChannel) {
        return channelId.equals(getChannel);
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
    public UUID getChannelId() {
        return channelId;
    }

    // 메세지 넣기

    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return userName + "/ createdAt : " + simpleDateFormat.format(createdAt) + "\n" + "[ " + content
                +" ]";

    }
}
