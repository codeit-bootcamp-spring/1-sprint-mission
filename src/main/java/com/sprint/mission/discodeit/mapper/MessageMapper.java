package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  public MessageDto toDto(Message message) {
    if (message == null) {
      return null;
    }

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        userMapper.toDto(message.getAuthor()),  // author는 UserDto로 변환
        message.getAttachments().stream()
            .map(attachment -> binaryContentMapper.toDto(attachment)) // 첨부파일 변환
            .collect(Collectors.toList())
    );
  }

}
