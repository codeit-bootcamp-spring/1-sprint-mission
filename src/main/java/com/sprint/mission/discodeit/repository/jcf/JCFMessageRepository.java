package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageRepository implements MessageRepository {
    //메시지는 중복 가능, 순서 중요
    private final List<Message> messageData = new ArrayList<>();

    @Override
    public Message saveMessage(Message message) {
        messageData.add(message);
        return message;
    }

    @Override
    public void deleteMessage(Message message) {
        messageData.remove(message);
    }

    @Override
    public List<Message> printChannel(Channel channel) {
        List<Message> result = new ArrayList<>();
        for (Message message : messageData) {
            if (message.getChannel().equals(channel)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> printByUser(User user) {
        List<Message> result = new ArrayList<>();
        for (Message message : messageData) {
            if (message.getName().equals(user)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> printAllMessage() {
        return new ArrayList<>(messageData);
    }
}