package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;

public record BinaryContentListResponse(
    List<BinaryContentResponse> binaryContentResponses
) {

  public static BinaryContentListResponse from(List<BinaryContent> contents) {
    List<BinaryContentResponse> responses = contents.stream()
        .map(BinaryContentResponse::from)
        .toList();

    return new BinaryContentListResponse(responses);
  }
}
