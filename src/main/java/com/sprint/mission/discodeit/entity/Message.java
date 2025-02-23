package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID channelId;
    private final UUID writerId;
    private String content;
    private List<UUID> attachmentIds;

    public Message(UUID channelId, String content, UUID writerId, List<UUID> attachmentIds) {
        super();
        this.channelId = channelId;
        this.writerId = writerId;
        this.content = content;
        this.attachmentIds = attachmentIds;
    }

    public void update(String newContent) {
        boolean isUpdated = false;
        if (!newContent.equals(this.content)) {
            this.content = newContent;
            isUpdated = true;
        }

        if (isUpdated) {
            updated();
        }
    }


}
