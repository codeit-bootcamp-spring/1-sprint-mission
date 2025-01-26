package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {
    private UUID id;
    private String name;

    // 채널명만 받는 예시
    public Channel(String name) {
        this.name = name;
    }

    // 직렬화용 기본 생성자
    public Channel() {}

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void update(String name) {
        this.name = name;
    }
}
