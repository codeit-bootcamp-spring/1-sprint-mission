package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;
  private final UserService userService;

  public MessageDto toDto(Message message) {
    User author = message.getAuthor();
    boolean isOnline = userService.isUserOnline(author);

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        //채널 fetch join
        message.getChannel().getId(),
        //유저 fetch join
        userMapper.toDto(message.getAuthor(), isOnline),

        //바이너리 컨텐츠는 batch size로 쿼리
        message.getAttachments().stream()
            .map(binaryContentMapper::toDto).toList()
    );
  }
  
}
