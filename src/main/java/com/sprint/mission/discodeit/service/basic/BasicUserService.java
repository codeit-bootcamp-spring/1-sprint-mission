package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private UserRepository userRepository;
    private ChannelService channelService;
    private MessageService messageService;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setService(ChannelService channelService, MessageService messageService) {
        this.channelService = channelService;
        this.messageService = messageService;
    }

    @Override
    public User createUser(String name, String email) {
        return null;
    }

    @Override
    public void updateUserName(UUID userId, String name) {

    }

    @Override
    public void updateUserEmail(UUID userId, String email) {

    }

    @Override
    public List<User> getAllUserList() {
        return null;
    }

    @Override
    public User searchById(UUID userId) {
        return null;
    }

    @Override
    public void deleteUser(UUID userId) {

    }
}
