package com.sprint.mission.discodeit.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String text;
    private final UUID authorId;
    private final UUID channelId;

    public void updateText(String text) {
        this.text = text;
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getText() {
        return text;
    }
    public String toString(){
        return "\nuuid: "+ id + " text: " + text + " authorId: " + authorId;
    }

    public Message(String text, UUID authorId, UUID channelId){
        this.authorId = authorId;
        this.channelId = channelId;
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.updatedAt = createdAt;
        this.text = text;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getChannelId() {
        return channelId;
    }
}
