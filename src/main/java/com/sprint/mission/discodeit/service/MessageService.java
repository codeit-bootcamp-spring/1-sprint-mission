package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message createMessage(UUID channelId, UUID writerId, String content);

    List<Message> getAllMessageList();

    Message searchById(UUID messageId);

    void updateMessage(UUID messageId, String content);

    void deleteMessage(UUID messageId);
}
