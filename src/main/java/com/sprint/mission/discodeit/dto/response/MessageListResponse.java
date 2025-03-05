package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;

//메시지 목록 응답
public record MessageListResponse(
    List<MessageResponse> messages,
    int totalCount //총 메시지 수
) {

  public static MessageListResponse from(List<Message> messages) {
    List<MessageResponse> dtos = messages.stream()
        .map(MessageResponse::from)
        .toList();

    return new MessageListResponse(dtos, dtos.size());
  }
}
