package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;

public interface MessageRepository {


    void saveAll(List<Message> messages);

    List<Message> loadAll();

    void reset();
}
