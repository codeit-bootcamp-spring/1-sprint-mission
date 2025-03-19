package com.spirnt.mission.discodeit.dto.message;


import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.enity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageDto {

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private String content;
  private UUID channelId;
  private UserDto author;
  private List<BinaryContentDto> attachments;

  @Builder
  public MessageDto(UUID id, Instant createdAt, Instant updatedAt, String content, UUID channelId,
      UserDto author, List<BinaryContentDto> attachments) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.content = content;
    this.channelId = channelId;
    this.author = author;
    this.attachments = attachments;
  }

  public static MessageDto from(Message message) {
    return MessageDto.builder()
        .id(message.getId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .content(message.getContent())
        .channelId(message.getChannel().getId())
        .author(UserDto.from(message.getAuthor()))
        .attachments(message.getAttachments().stream().map(BinaryContentDto::from).collect(
            Collectors.toList()))
        .build();
  }
}
