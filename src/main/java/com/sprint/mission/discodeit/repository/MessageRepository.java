package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageRepository {

    Message save(Message message);

    boolean delete(String messageId);

    Message findById(String id);

    List<Message> findAll();

    List<Message> findAllByChannelId(String channelId);
}
