package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    void createNewMessage(String title, String body);

    <T> String readMessage(T key);


    String readMessageAll();

    boolean updateMessageTitle(UUID ID, String change);

    boolean updateMessageTitle(String name, String change);

    boolean updateMessageBody(UUID ID, String change);

    boolean updateMessageBody(String name, String change);

    boolean deleteMessage(UUID id);

    boolean deleteMessage(String title);
    void addMessage(Message m);
    /// /////////////////////////////////////////////////////
}
