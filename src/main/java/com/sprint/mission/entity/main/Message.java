package com.sprint.mission.entity.main;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "메시지 엔티티")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createAt;
    private Instant updateAt;

    private UUID writerId;
    private UUID channelId;
    private List<UUID> attachmentIdList;
    private String content;

    public Message(UUID channelId, UUID userId, String content) {
        this.id = UUID.randomUUID();
        this.channelId = channelId;
        this.writerId = userId;
        this.content = content;
        this.createAt = Instant.now();
        this.attachmentIdList = new ArrayList<>();
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updateAt = Instant.now();
        }
    }

    public void refreshUpdateAt() {
        this.updateAt = Instant.now();
    }
}
