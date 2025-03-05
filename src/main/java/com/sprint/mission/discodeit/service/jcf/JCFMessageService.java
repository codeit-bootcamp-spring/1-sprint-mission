package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("jcfMessageService")
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {

    private final Map<UUID, MessageResponse> messageStorage = new HashMap<>();

    @Override
    public MessageResponse create(MessageCreateRequest messageCreateRequest) {
        MessageResponse messageResponse = new MessageResponse(
                UUID.randomUUID(),
                messageCreateRequest.getContent(),
                messageCreateRequest.getAuthorId(),
                messageCreateRequest.getChannelId(),
                null
        );
        messageStorage.put(messageResponse.getId(), messageResponse);
        return messageResponse; // ✅ 반환값 추가
    }

    @Override
    public void update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        MessageResponse messageResponse = messageStorage.get(messageId);
        if (messageResponse!= null) {
            messageResponse.setContent(messageUpdateRequest.getContent());
        }
    }

    @Override
    public void delete(UUID messageId) {
        messageStorage.remove(messageId);
    }

    @Override
    public List<MessageResponse> readAllByChannel(UUID channelId) {
        List<MessageResponse> result = new ArrayList<>();
        for (MessageResponse message : messageStorage.values()) {
            if (message.getChannelId().equals(channelId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<MessageResponse> readAll() {
        return new ArrayList<>(messageStorage.values());
    }
}
