package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message sendMessage(Message message);

    Message sendMessage(Message message, List<User> allUsers);

    List<Message> getChannelMessages(UUID channelId);

    List<Message> getUserMessages(UUID authorId);

    boolean editMessage(UUID id, String content);

    boolean deleteMessage(UUID id);
}
