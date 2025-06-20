package com.sprint.mission.discodeit.sse;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SseEvent {

    private final Long id;
    private final String name;
    private final Object Data;
    private final UUID userId;
    private final Instant createdAt;
}
