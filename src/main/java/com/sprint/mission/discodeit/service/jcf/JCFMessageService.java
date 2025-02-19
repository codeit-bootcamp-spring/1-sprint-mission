package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateDTO;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("jcfMessageService")
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {

    private final Map<UUID, MessageDTO> messageStorage = new HashMap<>();

    @Override
    public MessageDTO create(MessageCreateDTO messageCreateDTO) {
        MessageDTO messageDTO = new MessageDTO(
                UUID.randomUUID(),
                messageCreateDTO.getContent(),
                messageCreateDTO.getSenderId(),
                messageCreateDTO.getChannelId(),
                null
        );
        messageStorage.put(messageDTO.getId(), messageDTO);
        return messageDTO; // ✅ 반환값 추가
    }

    @Override
    public void update(UUID messageId, MessageUpdateDTO messageUpdateDTO) {
        MessageDTO messageDTO = messageStorage.get(messageId);
        if (messageDTO != null) {
            messageDTO.setContent(messageUpdateDTO.getContent());
        }
    }

    @Override
    public void delete(UUID messageId) {
        messageStorage.remove(messageId);
    }

    @Override
    public List<MessageDTO> readAllByChannel(UUID channelId) {
        List<MessageDTO> result = new ArrayList<>();
        for (MessageDTO message : messageStorage.values()) {
            if (message.getChannelId().equals(channelId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<MessageDTO> readAll() {
        return new ArrayList<>(messageStorage.values());
    }
}
