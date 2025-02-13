package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("fileMessageService")
public class FileMessageService implements MessageService {

    private final Map<UUID, MessageDTO> messages = new HashMap<>();

    @Override
    public void create(MessageDTO messageDTO) {
        messages.put(messageDTO.getId(), messageDTO);
    }

    // ✅ MessageCreateDTO를 직접 받을 수 있도록 추가
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

    // ✅ 메시지 삭제 메서드 추가
    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
    }
}
