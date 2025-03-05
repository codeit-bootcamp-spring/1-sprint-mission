package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;

public record ReadStatusListResponse(
    List<ReadStatusResponse> readStatusResponses
) {

  public static ReadStatusListResponse from(List<ReadStatus> readStatuses) {
    List<ReadStatusResponse> responses = readStatuses.stream()
        .map(ReadStatusResponse::from)
        .toList();

    return new ReadStatusListResponse(responses);
  }
}
