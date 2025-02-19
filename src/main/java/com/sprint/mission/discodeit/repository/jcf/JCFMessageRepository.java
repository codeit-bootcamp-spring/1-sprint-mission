package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jsf")
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new HashMap<>();
    private final Map<UUID, List<UUID>> channelMessages = new HashMap<>();

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        channelMessages.computeIfAbsent(message.getChannelId(), k -> new ArrayList<>()).add(message.getId()); // 채널별 메시지 추가
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Instant findLastMessageTimeByChannelId(UUID channelId) {
        List<UUID> messageIds = channelMessages.getOrDefault(channelId, Collections.emptyList());

        return messageIds.stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> messageIds = channelMessages.remove(channelId);
        if (messageIds != null) {
            messageIds.forEach(data::remove);
        }
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return channelMessages.getOrDefault(channelId, Collections.emptyList()).stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Message message) {
        data.remove(message.getId());
        channelMessages.getOrDefault(message.getChannelId(), new ArrayList<>()).remove(message.getId());
    }
}
