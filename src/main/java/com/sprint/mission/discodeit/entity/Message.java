package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;


@Getter
@ToString
public class Message {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String content;
    private User writer;
    private Channel channel;

    private Message(User writer, String content, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
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
        updatedAt = System.currentTimeMillis();
    }

}
