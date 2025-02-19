package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDTO create(MessageCreateDTO messageCreateDTO); // ✅ 반환 타입 변경 (void → MessageDTO)
    void update(UUID messageId, MessageUpdateDTO messageUpdateDTO);
    void delete(UUID messageId);
    List<MessageDTO> readAllByChannel(UUID channelId);
    List<MessageDTO> readAll();
}
