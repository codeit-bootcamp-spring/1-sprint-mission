package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    void CreateMessageDefault(String title, String body);

    <T> String ReadMessage(T key);


    String ReadMessageAll();

    void UpdateMessageTitle(UUID ID, String change);

    void UpdateMessageTitle(String name, String change);

    void UpdateMessageBody(UUID ID, String change);

    void UpdateMessageBody(String name, String change);

    void DeleteMessage(UUID id);

    void DeleteMessage(String title);
    void AddMessage(Message m);
    /// /////////////////////////////////////////////////////
}
