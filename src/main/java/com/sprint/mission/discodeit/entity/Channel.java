package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Channel extends BaseEntity {
    private String name;
    private String description;
    private UUID creatorId;
    private boolean isPrivate;
    private List<UUID> members = new ArrayList<>();

    public Channel(UUID id, String name, String description, UUID creatorId, boolean isPrivate, Instant createdAt, List<UUID> members) {
        super(id);
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        // 서버에서 createdAt 설정 (클라이언트 입력은 무시됨)
        setCreatedAt(createdAt != null ? createdAt : Instant.now());
        this.members = members != null ? members : new ArrayList<>();
    }

    // 공개 여부: isPrivate의 반대
    public boolean isPublic() {
        return !isPrivate;
    }

    // JSON 입력 시 "public" 필드 처리: 채널이 공개이면 isPrivate은 false로 설정
    @JsonSetter("public")
    public void setPublic(boolean publicValue) {
        this.isPrivate = !publicValue;
    }

    // JSON 응답 시 createdAt 값을 long (에포크 밀리초)로 반환
    @Override
    @JsonProperty("createdAt")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public Instant getCreatedAt() {
        return super.getCreatedAt();
    }
}
