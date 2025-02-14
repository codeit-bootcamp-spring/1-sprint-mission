package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

    private static final JCFMessageRepository jcfMessageRepository = new JCFMessageRepository();
    private final Map<UUID, Message> data;

    private JCFMessageRepository() {
        data = new HashMap<>(100);
    }

    public static JCFMessageRepository getInstance() {
        return jcfMessageRepository;
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 message입니다."));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        data.remove(messageId);
    }
}
