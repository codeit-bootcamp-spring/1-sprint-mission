package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message create(Message message);
    Optional<Message> read(User user);
    List<Message> readAll();
    Message updateByAuthor(User user, Message message);
    boolean delete(UUID id);
}