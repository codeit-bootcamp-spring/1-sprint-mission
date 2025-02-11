package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("basicMessageService")
@Primary
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final Map<UUID, MessageDTO> messages = new HashMap<>();

    @Override
    public void create(MessageDTO messageDTO) {
        messages.put(messageDTO.getId(), messageDTO);
    }

    @Override
    public void create(MessageCreateDTO messageCreateDTO) {
        MessageDTO messageDTO = new MessageDTO(
                UUID.randomUUID(),
                messageCreateDTO.getContent(),
                messageCreateDTO.getSenderId(),
                messageCreateDTO.getChannelId(),
                null
        );
        create(messageDTO);
    }

    @Override
    public List<MessageDTO> readAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public Optional<MessageDTO> read(UUID messageId) {
        return Optional.ofNullable(messages.get(messageId));
    }

    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
    }
}
