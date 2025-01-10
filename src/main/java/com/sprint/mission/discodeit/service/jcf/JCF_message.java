package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
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
    public void delete(Message message) {
        messageSet.remove(message);
    }

    @Override
    public void update(Message message, String updateMessage) {
        messageSet.forEach(messageContent -> {
            if (messageContent.getId().equals(message.getId())) {
                messageContent.updateMessage(updateMessage);
            }
        });
    }

    @Override
    public List<Message> write(User user, String title) {
        return messageSet.stream().filter(message_id ->
                        message_id.getUser().equals(user) && message_id.getChannel().getTitle().equals(title))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessage(String title) {
        return messageSet.stream().filter(message1 ->
                        message1.getChannel().getTitle().equals(title))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserMessage(User user) {
        messageSet.removeIf(message -> message.getUser().equals(user));
    }

    @Override
    public void deleteChannelMessage(Channel channel) {
        messageSet.removeIf(message -> message.getChannel().equals(channel));
    }
}
