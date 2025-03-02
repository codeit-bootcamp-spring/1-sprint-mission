package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface MessageRepository {
    Message save(Message message);

    Message findById(UUID messageId);

//    Message findBySenderId(UUID senderId);

    List<Message> findAllBySenderId(UUID senderId);


    List<Message> findAllByChannelId(UUID channelId);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    void deleteAllByChannelId(UUID channelId);
}
