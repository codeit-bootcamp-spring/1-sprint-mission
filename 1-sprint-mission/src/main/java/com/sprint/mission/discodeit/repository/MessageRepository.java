package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageRepository {
    void save(Message message);
    Message findByUuid(String messageUuid);
    List<Message>findAll();
    void delete(String messageUuid);

}
