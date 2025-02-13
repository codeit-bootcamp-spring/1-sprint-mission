package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO {
    private UUID id;
    private String name;
    private String description;
    private UUID creatorId;
    private boolean isPrivate;
    private Instant createdAt;
    private List<UUID> memberIds;

    // ✅ `createdAt`, `members` 포함하는 생성자 추가
    public ChannelDTO(UUID id, String name, String description, UUID creatorId, boolean isPrivate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.createdAt = Instant.now();
        this.memberIds = List.of();
    }

    public void setLastMessageTime(Instant lastMessageTime) {
        this.createdAt = lastMessageTime != null ? lastMessageTime : Instant.now();
    }
}
