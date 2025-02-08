package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;


import java.util.List;
import java.util.UUID;

public interface MessageService {

/*
    //DB로직
    UUID save(Message message);
    Message findOne(UUID id);
    List<Message> findAll();
    UUID update(Message message);
    UUID delete (UUID id);
*/

    //서비스 로직
    UUID create(String content, UUID authorId, UUID channelId);
    Message read(UUID id);
    List<Message> readAll();
    Message updateMessage(UUID id, String message, User user);
    UUID deleteMessage(UUID id, User user);
}