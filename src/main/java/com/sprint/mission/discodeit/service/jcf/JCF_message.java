package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_message implements MessageService {

    private final List<Message> messageSet;

    public JCF_message() {
        messageSet = new ArrayList<>();
    }

    @Override
    public void creat(Message message) {
        messageSet.add(message);

    }

    @Override
    public void delete(UUID messageId) {
        Optional<Message> getMessage = messageSet.stream().filter(message1 -> message1.getId().equals(messageId)).findFirst();
        Message message = getMessage.get();
        messageSet.remove(message);
    }

    @Override
    public void update(UUID messageId, String updateMessage) {
        messageSet.stream().filter(message -> message.getId().equals(messageId))
            .forEach(messageContent -> {{messageContent.updateMessage(updateMessage);
            }
        });
    }

    @Override
    public List<Message> write(UUID userId, UUID channelId) {
        return messageSet.stream().filter(message_id ->
                        message_id.isUserEqual(userId) && message_id.isChannelEqual(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessage(UUID channelId) {
        return messageSet.stream().filter(message1 ->
                        message1.isChannelEqual(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserMessage(UUID userId) {
        messageSet.removeIf(message -> message.isUserEqual(userId));
    }

    @Override
    public void deleteChannelMessage(UUID channelId) {
        messageSet.removeIf(message -> message.isChannelEqual(channelId));
    }
}
