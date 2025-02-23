package com.sprint.mission.discodeit.entity;



import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    // 초기에 자동 생성되는 필드들
    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    // 일반 필드들
    private ChannelType type;
    private String name;
    private String description;

    // 특정 필드만을 받아서 초기화하는 생성자
    public Channel(Instant updatedAt, ChannelType type, String name, String description) {
        this.updatedAt = updatedAt;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    // name, description 갱신 로직
    public void update(String newName, String newDescription) {
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
        }
        this.updatedAt = Instant.now();
    }
}
