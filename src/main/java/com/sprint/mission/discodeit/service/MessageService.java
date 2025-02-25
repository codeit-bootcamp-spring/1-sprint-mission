package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos);
    Message findById(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UUID messageId, MessageUpdateDto messageUpdateDto);
    void delete(UUID messageId);
    void deleteInChannel(UUID channelId); //채널 삭제 시 - 들어있던 모든 메세지 삭제 용

}
