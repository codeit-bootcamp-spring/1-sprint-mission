package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    void CreateMessageDefault(String title, String body);

    <T> String ReadMessage(T key);


    String ReadMessageAll();

    boolean UpdateMessageTitle(UUID ID, String change);

    boolean UpdateMessageTitle(String name, String change);

    boolean UpdateMessageBody(UUID ID, String change);

    boolean UpdateMessageBody(String name, String change);

    boolean DeleteMessage(UUID id);

    boolean DeleteMessage(String title);
    void AddMessage(Message m);
    /// /////////////////////////////////////////////////////
}
