package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("jcfMessageService")
public class JCFMessageService implements MessageService {

    private final Map<UUID, MessageDTO> messages = new ConcurrentHashMap<>();

    @Override
    public void create(MessageDTO messageDTO) {
        messages.putIfAbsent(messageDTO.getId(), messageDTO); // ✅ 동시성 안전성 강화
    }

    // ✅ MessageCreateDTO를 직접 받을 수 있도록 개선
    @Override
    public void create(MessageCreateDTO messageCreateDTO) {
        UUID messageId = UUID.randomUUID();
        messages.computeIfAbsent(messageId, id -> new MessageDTO(
                id,
                messageCreateDTO.getContent(),
                messageCreateDTO.getSenderId(),
                messageCreateDTO.getChannelId(),
                null
        ));
    }

    @Override
    public List<MessageDTO> readAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public Optional<MessageDTO> read(UUID messageId) {
        return Optional.ofNullable(messages.get(messageId));
    }

    // ✅ 메시지 삭제 메서드 (안전하게 삭제)
    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
    }
}
