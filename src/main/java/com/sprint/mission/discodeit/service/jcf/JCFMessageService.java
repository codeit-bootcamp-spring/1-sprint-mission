package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    // DB 대체 Message data
    private final List<Message> data = new ArrayList<>();

    @Override
    public void craete(UUID channelId, String message, UUID writer) {
        data.add(new Message(channelId, message, writer));
    }

    @Override
    public Message read(UUID id) {
        return data.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> allRead() {
        return data;
    }

    @Override
    public List<Message> allRead(UUID channelID) {
        return Arrays.asList((Message) data.stream()
                .filter(message -> message.getChannelId().equals(channelID)));
    }

    @Override
    public void updateMessage(UUID messageId, String updateMessage) {

        if (updateMessage.isBlank()){
            return;
        }

        data.stream()
                .filter(message -> message.getId().equals(messageId))
                .forEach(message -> {
                    message.updateMessage(updateMessage);
                });
    }

    @Override
    public void delete(UUID messageId) {
        Message delMessage = data.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .orElse(null);

        data.remove(delMessage);
    }
}
