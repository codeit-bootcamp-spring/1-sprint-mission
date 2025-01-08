package com.sprint.mission.discodeit.entity.common;

public enum Status {
    CREATED("생성"),
    MODIFY("수정"),
    ;

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
