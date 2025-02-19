package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final User user;
    private final Message message;
    private final byte[] data;
    private final String mimeType;

    public BinaryContent(User user, Message message, byte[] data, String mimeType) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.message = message;
        this.data = data;
        this.mimeType = mimeType;
    }
}
