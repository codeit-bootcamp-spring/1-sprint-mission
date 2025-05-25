package com.sprint.mission.discodeit.entity;

public enum Role {
    ROLE_USER(1, "일반 사용자"),
    ROLE_CHANNEL_MANAGER(2, "채널 매니저"),
    ROLE_ADMIN(3, "관리자");

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

    // 현재 권한이 요구된 권한을 가지고 있는지 확인하기
    public boolean hasAuthority(Role requiredRole) {
        return this.level >= requiredRole.level;
    }

    public boolean isHigherThan(Role otherRole) {
        return this.level > otherRole.level;
    }
}
