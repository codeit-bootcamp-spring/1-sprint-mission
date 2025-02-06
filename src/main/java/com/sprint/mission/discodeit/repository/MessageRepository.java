package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;

public interface MessageRepository {
    public void save(List<Message> messageList);
    public List<Message> load();
}
