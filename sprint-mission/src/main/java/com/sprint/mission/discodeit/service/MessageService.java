package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(String content, User user, Channel channel);
    Message getMessageById(UUID id);
    List<Message> getAllMessages();
    Message updateMessage(UUID id, String content);
    boolean deleteMessage(UUID id);
}
