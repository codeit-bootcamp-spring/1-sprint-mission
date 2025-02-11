package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Map;

public class BasicMessageService implements MessageService {
    @Override
    public void createMessage(Message message) {

    }

    @Override
    public void updateMessage(Message message, String content, User writer) {

    }

    @Override
    public void deleteMessage(Message message, User writer) {

    }

    @Override
    public void deleteChannelMessage(Channel channel) {

    }

    @Override
    public List<String> getAllMessage() {
        return List.of();
    }

    @Override
    public List<Map<String, String>> getChannelMessage(Channel channel) {
        return List.of();
    }

    @Override
    public List<Map<String, String>> getWriterMessage(User user) {
        return List.of();
    }
}
