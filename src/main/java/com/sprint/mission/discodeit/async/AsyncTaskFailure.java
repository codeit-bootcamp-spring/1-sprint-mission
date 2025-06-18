package com.sprint.mission.discodeit.async;

public record AsyncTaskFailure(String taskName, String requestId, String reason) {
}
