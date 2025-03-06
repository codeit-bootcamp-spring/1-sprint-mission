package com.sprint.mission.entity.main;

import com.sprint.mission.config.BaseTimeEntity;
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
@Setter @Schema(description = "메시지 엔티티")
public class Message  extends BaseTimeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;

    private UUID writerId;
    private UUID channelId;
    private List<UUID> attachmentIdList;
    private String content;

    public Message(UUID channelId, UUID userId, String content) {
        this.id = UUID.randomUUID();
        this.channelId = channelId;
        this.writerId = userId;
        this.content = content;
        this.attachmentIdList = new ArrayList<>();
    }

    public void update(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
        }
    }
}
