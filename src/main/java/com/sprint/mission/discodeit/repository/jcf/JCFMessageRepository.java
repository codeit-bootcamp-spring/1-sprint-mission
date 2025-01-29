package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new HashMap<>();

    public void createMessage(Message message) {
        data.put(message.getId(), message);
    }

    public Optional<Message> getMessage(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    public List<Message> getAllMessages() {
        return new ArrayList<>(data.values());
    }

    public void updateMessage(UUID id, String content) {
        Message message = data.get(id);
        if (message == null) {
            throw new IllegalArgumentException("해당 iD의 메시지를 찾을 수 없습니다: " + id);
        }
        message.update(content);
    }

    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
