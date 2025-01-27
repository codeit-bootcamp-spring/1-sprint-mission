package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, User writer, Channel channel);

    List<Message> getMessageByUser(User writer);

    List<Message> getMessageByChannel(Channel channel);

    Message updateMessageContent(Message message, String newContent);

    void deleteMessageByWriter(User writer, UUID uuid);

    void deleteMessageByChannel(Channel channel, UUID uuid);
}
