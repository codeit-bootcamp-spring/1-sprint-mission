package com.spirnt.mission.discodeit.async;

/**
 * 비동기 메서드가 재시도 횟수를 초과하며 실패한 경우의 실패 정보
 */
public record AsyncTaskFailure(
    String taskName,
    String requestId,
    String failureReason
) {

}
