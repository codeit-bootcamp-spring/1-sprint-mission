package com.sprint.mission.discodeit.service.interfacepac;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponseDTO create(MessageCreateDTO messageCreateDTO);
    Message find(User user);
    List<Message> findAll();
    List<MessageResponseDTO> findMessagesByChannelId(UUID channelId);
    MessageResponseDTO update(MessageUpdateDTO updateDTO);
    void delete(UUID messageId);
}
