package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Message extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID channelId;
    private UUID writerId;
    private String content;

    @Builder
    public Message(UUID channelId, UUID writerId, String content) {
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
