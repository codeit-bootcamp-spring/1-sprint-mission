package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.*;

public interface MessageRepository {
    void save(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findAll();
    void delete(UUID id);
}