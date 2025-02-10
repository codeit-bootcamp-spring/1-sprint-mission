package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable, Entity {

    private UUID id;
    private Instant createdAt;
    private User author;
    private Channel channel;
    private File content;

    public BinaryContent(User author, Channel channel, File content) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.author = author;
        this.channel = channel;
        this.content = content;
    }


}
