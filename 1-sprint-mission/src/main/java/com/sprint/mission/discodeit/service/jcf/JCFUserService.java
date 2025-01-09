package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();
//    private final JCFMessageService messageService;
//    private final JCFChannelService channelService;
//
//    public JCFUserService(JCFMessageService messageService, JCFChannelService channelService) {
//        this.messageService = messageService;
//        this.channelService = channelService;
//    }

    @Override
    public void create(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User read(UUID userId) {
        return data.get(userId);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID userId, String username) {
        User user = data.get(userId);
        if (user != null) {
            user.update(username);
            data.put(userId, user);
        }
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
    }
}
