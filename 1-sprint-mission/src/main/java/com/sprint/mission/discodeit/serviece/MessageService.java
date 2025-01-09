package com.sprint.mission.discodeit.serviece;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.UUID;

public interface MessageService {
    void CreateMessage(String title, String body);

    String ReadMessage(UUID ID);

    String ReadMessage(String title);

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
