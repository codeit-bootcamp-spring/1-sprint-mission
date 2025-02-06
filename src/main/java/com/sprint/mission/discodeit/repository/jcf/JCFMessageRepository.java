package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

//@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID,Message> data;

    public JCFMessageRepository() {
        data = new HashMap<>();
    }


    @Override
    public Message save(Message message) {
        data.put(message.getId(),message);
        return message;
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<Message> getAllMessage() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteMessage(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("Message with id "+id+" not found");
        }
        data.remove(id);
    }

    @Override
    public Optional<Instant> findLastMessageTimeByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getCreatedAt)
                .max(Instant::compareTo);
    }

    @Override
    public List<Message> getMessagesByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> messageIds = data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getId)
                .toList();

        if (messageIds.isEmpty()) {
            throw new NoSuchElementException("No messages found for channel ID: " + channelId);
        }

        messageIds.forEach(data::remove);
    }
}
