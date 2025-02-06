package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findAll();

    boolean existsByUser(User user);
    boolean existsByChannel(Channel channel);
    void deleteByMessage(Message message);
}
