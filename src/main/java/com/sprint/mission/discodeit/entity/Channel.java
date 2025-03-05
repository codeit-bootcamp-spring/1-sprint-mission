package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sprint.mission.discodeit.util.InstantDeserializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private UUID creatorId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "channel_members", joinColumns = @JoinColumn(name = "channel_id"))
    @Column(name = "member_id")
    private List<UUID> members = new ArrayList<>();

    @CreationTimestamp
    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @Column(updatable = false, nullable = false)
    private Instant createdAt = Instant.now();

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean isPrivate;

    // JPA 기본 생성자 (필수)
    public Channel(String name, String description, UUID creatorId, boolean isPrivate, List<UUID> members) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.members = members != null ? members : new ArrayList<>();
        this.createdAt = Instant.now();
    }

    // DTO 변환을 위한 생성자
    public Channel(UUID id, String name, String description, UUID creatorId, boolean isPrivate, Instant createdAt, List<UUID> members) {
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.members = members != null ? members : new ArrayList<>();
    }

    // 공개 여부 반환 (isPrivate의 반대 개념)
    public boolean isPublic() {
        return !isPrivate;
    }

    // JSON 입력 시 "public" 필드 처리 (채널이 공개이면 isPrivate을 false로 설정)
    @JsonSetter("public")
    public void setPublic(boolean publicValue) {
        this.isPrivate = !publicValue;
    }
}
