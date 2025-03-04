package com.sprint.mission.discodeit.entity;

public enum OnlineStatus {
    ONLINE("온라인"),
    OFFLINE("오프라인");

    private final String displayName;

    OnlineStatus(String displayName) {
        this.displayName = displayName;
    }
}
