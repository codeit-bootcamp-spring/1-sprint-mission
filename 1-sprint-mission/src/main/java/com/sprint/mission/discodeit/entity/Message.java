package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private final Instant createdAt = Instant.now();
    private Instant updatedAt;
    //
    private User user;
    private Channel channel;
    private String content;


    public Message(User user, Channel channel, String content) {
        this.user = user;
        this.channel = channel;
        this.content = content;
        this.id = UUID.randomUUID();
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
