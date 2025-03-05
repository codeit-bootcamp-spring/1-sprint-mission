package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    Message save(Message message);
    void deleteById(String id);
    Optional<Message> findById(String id);
    List<Message> findAll();
    List<Message> findAllByChannelId(String channelId);
}