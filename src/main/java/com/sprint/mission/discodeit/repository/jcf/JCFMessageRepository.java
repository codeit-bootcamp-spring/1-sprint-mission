package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class JCFMessageRepository implements MessageRepository {
    private final Map<Long, Message> data;
    private final AtomicLong idGenerator = new AtomicLong(1);   // ID 초기값 1

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long saveMessage(Message message) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, message);
        return id;
    }

    @Override
    public Message loadMessage(Long id) {
        return data.get(id);
    }

    @Override
    public Map<Long, Message> loadAllMessages() {
        return data;
    }

    @Override
    public void deleteMessage(Long id) {
        if (data.remove(id) == null) {
            throw new IllegalArgumentException("삭제할 메시지가 존재하지 않습니다.");
        }
    }

    @Override
    public void updateMessage(Long id, Message message) {
        data.put(id, message);
    }
}
