package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;

public record MessageResponseDto(
    //객체 식별 id
    String id,
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

  public static MessageResponseDto from(Message message) {
    return new MessageResponseDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getSenderId(),
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
