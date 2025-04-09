package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageMapper {

  public MessageDto toDto(Message message) {
    if (message == null) {
      log.warn("메시지를 입력해주세요.");
      throw new RestApiException(DomainErrorCode.MESSAGE_NOT_FOUND, "메시지를 입력해주세요.");
    }

    return MessageDto.builder()
        .id(message.getId())
        .content(message.getContent())
        .build();
  }

}
