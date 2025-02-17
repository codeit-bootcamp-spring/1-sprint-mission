package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageReplsitory implements MessageRepository {

    private final Map<UUID, Map<UUID, Message>> data = new HashMap<>();

    @Override
    public Message save(Message message) {
        data.putIfAbsent(message.getChannelId(), new HashMap<>());
        data.get(message.getChannelId()).put(message.getId(), message);
        return null;
    }

    @Override
    public Optional<Message> findById(UUID uuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(uuid)) {
                return Optional.ofNullable(channelMessages.get(uuid));
            }
        }
        System.out.println("Message with ID " + uuid + " not found.");
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        List<Message> allMessages = new ArrayList<>();
        for (Map<UUID, Message> channelMessages : data.values()) { // 채널별 메시지 맵 가져오기
            allMessages.addAll(channelMessages.values()); // 메시지들을 리스트에 추가
        }
        return allMessages;
    }

    @Override
    public void deleteById(UUID msgUuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(msgUuid)) {
                channelMessages.remove(msgUuid);
                System.out.println("Message " + msgUuid + " deleted.");
            }
        }
    }
    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
