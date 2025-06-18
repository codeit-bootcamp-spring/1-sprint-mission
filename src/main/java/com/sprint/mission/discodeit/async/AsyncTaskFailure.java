package com.sprint.mission.discodeit.async;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AsyncTaskFailure {

    private final String taskName;
    private final String requestId;
    private final String failureReason;
}
