package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface MessageService {

    Message CreateMsg(User user1, Channel channel1, String content);

    Message getMessage(UUID msgId);

    Map<UUID, Map<UUID, Message>> getAllMsg();

    void updateMsg(UUID msgId, String newContent);

    void deleteMsg(UUID msgId);

}
