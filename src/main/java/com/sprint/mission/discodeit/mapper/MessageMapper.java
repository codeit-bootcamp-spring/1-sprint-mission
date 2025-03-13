package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageMapper {
  private final UserMapper userMapper;
  public MessageDto toDto(Message message) {
    if (message == null) {
      return null;
    }
    UserDto userDto = userMapper.toDto(message.getAuthor());
    List<BinaryContentDto> binaryContentDtos = new ArrayList<>();
    for (BinaryContent attachment : message.getAttachments()) {
      binaryContentDtos.add(BinaryContentMapper.toDto(attachment));
    }
    return new MessageDto(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
        message.getContent(), message.getChannel().getId(), userDto, binaryContentDtos);
  }

}
