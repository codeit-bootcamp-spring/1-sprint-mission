package com.sprint.mission.discodeit.global.monitoring;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncTaskFailure {

    private final String taskName;
    private final String requestId;
    private final String failureReason;

    @Override
    public String toString() {
        return "AsyncTaskFailure{" +
            "taskName='" + taskName + '\'' +
            ", requestId='" + requestId + '\'' +
            ", failureReason='" + failureReason + '\'' +
            '}';
    }
}
