package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;

public interface MessageService {

    void createMessage(Message message);
    void updateMessage(Message message, String content, User writer);
    void deleteMessage(Message message, User writer);
    List<String> getAllMessage();
    List<Map<String, String>> getChannelMessage(Channel channel);
    List<Map<String, String>> getWriterMessage(User user);

}
