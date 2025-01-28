package com.sprint.mission.discodeit.entity.common;

public enum Status {

    MODIFIED("수정"),
    REGISTERED("등록"),
    UNREGISTERED("해지")
    ;

    private final String description;

    Status(String status) {
        this.description = status;
    }

    public String getDescription() {
        return description;
    }
}
