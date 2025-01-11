package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;

public interface MessageService {

    void addChannelMsg(Message msg);

    Message getMessage(UUID msgId);

    Map<UUID, Map<UUID, Message>> getAllMsg();

    void updateMsg(UUID msgId, String newContent);

    void deleteMsg(UUID msgId);


}
