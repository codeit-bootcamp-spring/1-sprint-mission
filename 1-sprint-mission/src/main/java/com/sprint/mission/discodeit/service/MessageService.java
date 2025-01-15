package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    void createMessage(Message message);
    Message readMessage(String messageUuid);
    List<Message> readAllMessages();
    void updateMessage(String messageUuid, String newMessageText);
    void deleteMessage(String messageUuid);
}