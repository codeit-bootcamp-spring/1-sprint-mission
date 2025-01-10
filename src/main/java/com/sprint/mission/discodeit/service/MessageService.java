package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    public void Creat(Message message);

    public void Delete(Message message);

    public void Update(Message message, Message updateMessage);

    public List<Message> Write(UUID id);

    public List<Message> AllWrite();
}
