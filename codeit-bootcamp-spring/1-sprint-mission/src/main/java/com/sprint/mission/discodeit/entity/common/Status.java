package com.sprint.mission.discodeit.entity.common;

public enum Status {

    MODIFIED("수정"),
    REGISTERED("등록"),
    UNREGISTERED("해지")
    ;

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
