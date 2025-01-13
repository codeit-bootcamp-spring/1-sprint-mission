package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    // DB 대체 Message data
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public void craete(UUID channelId, String message, UUID writer) {

        Message newMessage = new Message(channelId, message, writer);

        data.put(newMessage.getId(), newMessage);
    }

    @Override
    public Message read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> allRead() {
        return data.values().stream().toList();
    }

    @Override
    public List<Message> allRead(UUID channelID) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelID))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String updateMessage) {

        if (updateMessage.isBlank()){
            return;
        }

        data.get(id).updateMessage(updateMessage);
    }

    @Override
    public void delete(UUID id) {

        data.remove(id);
    }
}