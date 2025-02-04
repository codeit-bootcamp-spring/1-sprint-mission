package com.sprint.mission.discodeit.util.time;

import java.time.Instant;

public class SystemTimeProvider implements TimeProvider{
    @Override
    public Instant getCurrentTime() {
        return Instant.now();
    }
}
