package com.sprint.mission.discodeit.entity;

public enum Role {
    USER(1, "일반 사용자"),
    CHANNEL_MANAGER(2, "채널 매니저"),
    ADMIN(3, "관리자");

    private final int level;
    private final String description;

    Role(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }
    
}
