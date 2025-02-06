package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryContent {
    private UUID id;
    private User user;
    private byte[] data;

    public BinaryContent(User user, byte[] data) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.data = data;
    }
}
