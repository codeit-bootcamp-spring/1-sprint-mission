package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    //데이터를 저장할 수 있는 필드 -> Map 사용
    private final Map<UUID, Message> data = new HashMap<>();
    @Override
    public Message createMessage(String content, UUID userId, UUID channelId) {
        Message message = new Message(content, userId, channelId);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message getMessageById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMessage(UUID id, String content) {
        Message message = data.get(id);
        if(message != null) {
            message.updateContent(content);
        } else {
            throw new NoSuchElementException("Message를 찾을 수 없습니다");
        }

    }

    @Override
    public void deleteMessage(UUID id) {
        if(data.containsKey(id)) {
            data.remove(id);
        } else {
            throw new NoSuchElementException("Message를 찾을 수 없습니다.");
        }

    }
}
