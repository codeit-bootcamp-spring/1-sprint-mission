package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService extends CRUDService<Message> {
    List<Message> readAll(UUID id);
}
