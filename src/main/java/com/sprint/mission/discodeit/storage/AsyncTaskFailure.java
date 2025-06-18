package com.sprint.mission.discodeit.storage;

public record AsyncTaskFailure(String taskName, String requestId, String failureReason) {

}
