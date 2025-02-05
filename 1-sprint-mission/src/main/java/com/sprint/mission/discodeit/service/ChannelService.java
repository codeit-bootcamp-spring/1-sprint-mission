package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    Channel create(User user, String name, String description);
    Channel find(User user);
    List<Channel> findAll();
    Channel update(Channel channel, String newName, String newDescription);
    void delete(Channel channel);

}
