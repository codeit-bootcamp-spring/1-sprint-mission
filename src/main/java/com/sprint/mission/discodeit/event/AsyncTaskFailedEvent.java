package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AsyncTaskFailedEvent {

    private final AsyncTaskFailure failure;
}
