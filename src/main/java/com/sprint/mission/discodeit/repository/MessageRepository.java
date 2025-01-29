package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;

public interface MessageRepository {

    public Long saveMessage(Message message);

    public Message loadMessage(Long id);

    public Map<Long, Message> loadAllMessages();

    public void deleteMessage(Long id);

    public void updateMessage(Long id, Message message);
}
