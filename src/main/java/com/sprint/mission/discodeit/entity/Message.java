package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID channelId;
    private final UUID writerId;
    private String content;

    public Message(UUID channelId, String content, UUID writerId) {
        super();
        this.channelId = channelId;
        this.writerId = writerId;
        this.content = content;
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            updated();
        }
    }


}
