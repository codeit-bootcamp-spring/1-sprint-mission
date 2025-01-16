package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    public void creat(Message message);

    public void delete(Message message);

    public void update(Message message, String updateMessage);

    public List<Message> write(User user, String title);

    public List<Message> getMessage(String title);

    public void deleteUserMessage(User user);

    public void deleteChannelMessage(Channel channel);
}
