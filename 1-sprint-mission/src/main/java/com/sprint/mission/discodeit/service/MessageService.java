package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {
    Message create(User user, Channel channel, String content);
    Message find(User user);
    List<Message> findAll();
    Message update(User user, Channel channel, String content);
    void delete(User user, Channel channel, Message message);
}
