package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(MessageCreateDTO messageCreateDTO);
    Message find(UUID id);
    List<Message> findAllByChannelId(UUID channelId);
    List<Message> findAll();
    Message update(MessageUpdateDTO messageUpdateDTO);
    void deleteMessage (UUID msgID);


}
