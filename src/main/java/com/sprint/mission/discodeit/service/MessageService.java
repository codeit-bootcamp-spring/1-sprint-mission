package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message createMessage(Channel channel, User writer, String content);

    List<Message> getAllMessageList();

    Message searchById(UUID messageId);

    void printMessageInfo(Message message);
    void printMessageListInfo(List<Message> messageList);

    void deleteMessage(Message message);
    void updateMessage(Message message, String content);
}
