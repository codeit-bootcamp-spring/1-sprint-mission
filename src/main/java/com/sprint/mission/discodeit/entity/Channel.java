package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Instant createdAt = Instant.now();  // 생성 시점에 Instant.now() 기본값 설정

    private Instant updatedAt;
    private ChannelType type;
    private String name;
    private String description;

    // 새로 추가된 생성자
    public Channel(ChannelType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.createdAt = Instant.now();  // createdAt은 자동으로 설정
        this.updatedAt = this.createdAt;  // 업데이트 날짜는 생성 시점으로 초기화
    }

    // update 메서드에서 updatedAt 갱신
    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();  // 수정 시점으로 updatedAt을 갱신
        }
    }
}
