package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {
    private final Map<Long, Message> data;
    private final AtomicLong idGenerator = new AtomicLong(1);   // ID 초기값 1

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long save(Message message) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, message);
        return id;
    }

    @Override
    public Message load(Long id) {
        return data.get(id);
    }

    @Override
    public Map.Entry<Long, Message> load(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    @Override
    public Map<Long, Message> loadAll() {
        return data;
    }

    @Override
    public Long delete(Long id) {
        if (data.remove(id) == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        return id;
    }

    @Override
    public void update(Long id, Message message) {
        data.put(id, message);
    }

    @Override
    public Map<Long, Message> findMessagesByChannelId(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getChannelId().equals(uuid))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        findMessagesByChannelId(channelId).keySet().forEach(this::delete);
    }

}
