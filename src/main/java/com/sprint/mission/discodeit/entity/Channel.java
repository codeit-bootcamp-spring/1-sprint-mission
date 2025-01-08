package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends BaseEntity {
    private String name;
    private String description;

    // 생성자
    public Channel(String name, String description) {
        super(); // BaseEntity의 생성자 호출
        this.name = name;
        this.description = description;
    }

    // Getter 메서드
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // 필드 업데이트 메서드
    public void update(String name, String description) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        updateTimestamp(); // 수정 시간 갱신
    }
}
