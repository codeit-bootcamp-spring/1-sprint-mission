package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Message;

import com.sprint.mission.discodeit.entity.User;
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
    UserDto author,
    //메세지 내용
    String content,
    //메세지가 생성된 채널
    UUID channelId,
    //첨부파일
    List<BinaryContentDto> attachments
) {

  public static MessageDto from(Message message) {
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        UserDto.from(message.getAuthor(), message.getAuthor().getUserStatus().isActive()),
        message.getContent(),
        message.getChannel().getId(),
        message.getAttachments().stream().map(BinaryContentDto::from).toList()
    );
  }
}
