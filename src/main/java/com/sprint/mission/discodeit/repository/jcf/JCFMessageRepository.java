package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {
    Map<UUID, Message> messageMap = new HashMap<>();

    @Override
    public Message save(Message message) {
        messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findByMessageId(UUID messageId) {
        return messageMap.get(messageId);  // 메시지 아이디로 메시지 찾기
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return messageMap.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        messageMap.remove(messageId);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        messageMap.values().removeIf(message -> message.getChannelId().equals(channelId));
    }

}
