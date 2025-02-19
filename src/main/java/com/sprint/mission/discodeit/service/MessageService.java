package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(MessageDTO messageDTO);
    Message findById(UUID messageId);
    List<Message> findAllMessage();
    void update(UUID messageId, MessageDTO messageDTO);
    void delete(UUID messageId);
    void deleteInChannel(UUID channelId); //채널 삭제 시 - 들어있던 모든 메세지 삭제 용

}
