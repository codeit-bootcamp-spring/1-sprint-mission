package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;

public interface MessageService {
    Message create(Message message);
    Message findById(String id);
    List<Message> findAll();
    Message update(String id, Message message);
    void delete(String id);
}
