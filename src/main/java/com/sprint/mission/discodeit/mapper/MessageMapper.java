package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Component
public class MessageMapper {
  public Message toEntity(CreateMessageDto dto) {
    List<BinaryContent> contents = convertMultipartFileToBinaryContent(
        dto.getUserId(),
        dto.getChannelId(),
        dto.getMultipart()
    );

    return new Message.MessageBuilder(
        dto.getUserId(),
        dto.getChannelId(),
        dto.getContent()
    )
        .binaryContents(contents)
        .build();
  }



  public MessageResponseDto toDto(Message message, List<BinaryContent> contents){
    List<String> base64Encoded = contents.stream().map(content -> {
      return Base64.getEncoder().encodeToString(content.getData());
    }).toList();

    return new MessageResponseDto(
        message.getUUID(),
        message.getUserUUID(),
        message.getChannelUUID(),
        message.getContent(),
        message.getCreatedAt(),
        base64Encoded
    );
  }

  private List<BinaryContent> convertMultipartFileToBinaryContent(String userId, String channelId, List<MultipartFile> files) {
    if (files == null || files.isEmpty()) {
      return Collections.emptyList();
    }

    return files.stream().map(file -> {
      try {
        return new BinaryContent.BinaryContentBuilder(
            userId,
            file.getName(),
            file.getContentType(),
            file.getSize(),
            file.getBytes()
        )
            .channelId(channelId)
            .build();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).toList();
  }

}
