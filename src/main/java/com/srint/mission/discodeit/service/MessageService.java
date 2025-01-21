package com.srint.mission.discodeit.service;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;


import java.util.List;
import java.util.UUID;

public interface MessageService {

    //DB로직
    UUID save(Message message);
    Message findOne(UUID id);
    List<Message> findAll();
    UUID delete (UUID id);

    //서비스 로직
    UUID create(String content, User user, Channel channel);
    Message read(UUID id);
    List<Message> readAll();
    Message update(UUID id, String message, User user);
    UUID deleteMessage(UUID id, User user);
}
