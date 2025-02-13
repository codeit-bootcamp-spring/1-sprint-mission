package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFMessageRepository implements MessageRepository {
    Map<UUID, Message> store;

    public JCFMessageRepository() {
        this.store = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        store.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(store.get(id));  // 메시지 아이디로 메시지 찾기
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return store.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        store.values().removeIf(message -> message.getChannelId().equals(channelId));
    }

}
