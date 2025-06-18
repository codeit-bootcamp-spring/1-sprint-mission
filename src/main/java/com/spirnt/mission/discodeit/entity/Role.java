package com.spirnt.mission.discodeit.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_CHANNEL_MANAGER("채널 매니저"),
    ROLE_USER("일반 사용자");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
}
