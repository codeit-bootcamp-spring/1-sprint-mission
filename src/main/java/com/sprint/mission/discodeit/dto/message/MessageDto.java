package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    //객체 식별 id
    UUID id,
    //생성 날짜
    Instant createdAt,
    //수정 시간
    Instant updatedAt,
    //메세지 작성자
    String authorId,
    //메세지 내용
    String content,
    //메세지가 생성된 채널
    String channelId,
    //첨부파일
    List<String> attachmentIds
) {

  public static MessageDto from(Message message) {
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getAuthorId(),
        message.getContent(),
        message.getChannelId(),
        message.getAttachmentImageIds()
    );
  }

  @Override
  public String toString() {
    return "[MessageResponseDto] " +
        "{id:" + id
        + " authorId:" + authorId
        + " content:" + content
        + " channelId:" + channelId
        + " createdAt:" + createdAt
        + " updatedAt:" + updatedAt;
  }
}
