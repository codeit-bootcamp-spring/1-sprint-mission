package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String text;
    private final UUID authorId;
    private final UUID channelId;

    public Message(String text, UUID authorId, UUID channelId){
        this.authorId = authorId;
        this.channelId = channelId;
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.text = text;
    }

    public void updateText(String text) {
        this.text = text;
        this.updatedAt = Instant.now();
    }

    public String toString(){
        return "\nuuid: "+ id + " text: " + text + " authorId: " + authorId;
    }
}
