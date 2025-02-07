package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
@ToString
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String content;
    private User writer;
    private Channel channel;

    private Message(User writer, String content, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.writer = writer;
        this.content = content;
        this.channel = channel;
    }

    public static Message of(User writer, String content, Channel channel) {
        return new Message(writer, content, channel);
    }

    public void updateContent(String content) {
        this.content = content;
        updatedAt = Instant.now();
    }

}
