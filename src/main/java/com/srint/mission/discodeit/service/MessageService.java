package com.srint.mission.discodeit.service;

import com.srint.mission.discodeit.entity.Message;


import java.util.List;
import java.util.UUID;

public interface MessageService {

    UUID create(Message message);

    Message read(UUID id);

    List<Message> readAll();

    Message update(UUID id, Message Message);

    UUID delete(UUID id);
}
