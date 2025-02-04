package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageReplsitory implements MessageRepository {

    private final Map<UUID, Map<UUID, Message>> data = new HashMap<>();

    public void save(Message message) {
        data.putIfAbsent(message.getDestinationCh().getChanneluuId(), new HashMap<>());
        data.get(message.getDestinationCh().getChanneluuId()).put(message.getUuId(), message);
    }

    @Override
    public Message findById(UUID uuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(uuid)) {
                return channelMessages.get(uuid);
            }
        }
        System.out.println("Message with ID " + uuid + " not found.");
        return null;
    }

    @Override
    public Map<UUID, Map<UUID, Message>> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID msgUuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(msgUuid)) {
                channelMessages.remove(msgUuid);
                System.out.println("Message " + msgUuid + " deleted.");
            }
        }
    }

    @Override
    public void deleteAllMessagesForChannel(UUID channelId){
        if (!data.containsKey(channelId)) {
            System.out.println("No messages found for channel ID");
        }
        data.remove(channelId);
        System.out.println("All messages for channel "+ "'" + channelId +"'" +" have been deleted.");
    }
}
