package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String content;
    private User sender;
    private Channel channel;

    private final Instant createdAt;
    private Instant updatedAt;

    public Message(String content, Channel channel, User sender) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.channel = channel;
        this.sender = sender;
        this.createdAt = Instant.now();
    }


    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    public String toString() {
        return "Message {" +
                "id = " + id +
                ", content = " + content +
                ", channel = " + channel.getChannelName() +
                ", sender = " + sender.getUsername() +
                ", createdAt = " + createdAt +
                ", updatedAt = " + updatedAt + "}";
    }

}