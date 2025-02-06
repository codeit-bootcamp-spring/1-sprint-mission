package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryContent {
    private UUID id;
    private User user;
    private Message message;
    private byte[] data;

    public BinaryContent(User user, Message message) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.message = message;
        this.data = data;
    }
}
