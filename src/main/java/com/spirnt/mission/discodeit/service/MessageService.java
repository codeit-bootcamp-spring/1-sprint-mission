package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService extends CRUDService<Message> {
    List<Message> readAll(UUID id);
}
