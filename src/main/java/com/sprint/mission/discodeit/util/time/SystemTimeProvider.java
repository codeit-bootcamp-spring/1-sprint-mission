package com.sprint.mission.discodeit.util.time;

public class SystemTimeProvider implements TimeProvider{
    @Override
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
