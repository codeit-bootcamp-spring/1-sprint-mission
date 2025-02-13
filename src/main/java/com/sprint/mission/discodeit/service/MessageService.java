package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void create(MessageDTO messageDTO);
    void create(MessageCreateDTO messageCreateDTO);
    List<MessageDTO> readAll();
    Optional<MessageDTO> read(UUID messageId);
    void delete(UUID messageId);
}
