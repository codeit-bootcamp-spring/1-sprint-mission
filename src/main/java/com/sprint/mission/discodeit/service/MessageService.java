package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    public void creat(String content, UUID userId, UUID channelId);

    public void delete(UUID messageId);

    public void update(UUID messageId, String updateMessage);

    List<Message> write(UUID userId, UUID channelId);

    public List<Message> getMessage(UUID channelId);

    public void deleteUserMessage(UUID userID);

    public void deleteChannelMessage(UUID channelId);
}
