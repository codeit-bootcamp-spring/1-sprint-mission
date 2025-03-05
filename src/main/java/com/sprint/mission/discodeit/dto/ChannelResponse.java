package com.sprint.mission.discodeit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ChannelResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID creatorId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    private List<UUID> memberIds;

    @JsonProperty("private")
    private boolean privateChannel;

    // 엔티티 기반 생성자
    public ChannelResponse(com.sprint.mission.discodeit.entity.Channel channel) {
        this.id = channel.getId();
        this.name = channel.getName();
        this.description = channel.getDescription();
        this.creatorId = channel.getCreatorId();
        this.createdAt = channel.getCreatedAt();
        this.updatedAt = channel.getUpdatedAt();
        this.memberIds = channel.getMembers();
        this.privateChannel = channel.isPrivate();
    }

    // 오버로딩 생성자: (id, name, description, creatorId, privateChannel, createdAt, memberIds)
    public ChannelResponse(UUID id, String name, String description, UUID creatorId, boolean privateChannel, Instant createdAt, List<UUID> memberIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.privateChannel = privateChannel;
        this.createdAt = createdAt;
        this.updatedAt = createdAt; // 초기값으로 설정
        this.memberIds = memberIds;
    }
}

