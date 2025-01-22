package com.sprint.mission.discodeit.service.basic.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BasicJCFUserService implements UserService {
    private final UserRepository userRepository;
    private MessageService messageService;
    private ChannelService channelService;

    public BasicJCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setDependencies(MessageService messageService, ChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    @Override
    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User ID already exists: " + user.getId());
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> readUser(User user) {
        return userRepository.findById(user.getId());
    }

    @Override
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User existUser, User updateUser) {
        Optional<User> userOptional = userRepository.findById(existUser.getId());
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        existUser.updateTime();
        existUser.updateUserid(updateUser.getUserid());
        existUser.updateUsername(updateUser.getUsername());
        existUser.updatePassword(updateUser.getPassword());
        existUser.updateUserEmail(updateUser.getEmail());

        return userRepository.save(existUser);
    }

    @Override
    public boolean deleteUser(User user) {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isEmpty()) {
            return false;
        }

        // 사용자 관련 메시지 삭제
        messageService.deleteMessageByUser(user);

        // 모든 채널에서 사용자 제거
        channelService.readAllChannels().forEach(channel -> {
            if (channel.getParticipants().contains(user)) {
                channel.getParticipants().remove(user);
                System.out.println("채널 '" + channel.getName() + "'에서 사용자" +
                        "'" + user.getUsername() + "' 제거 완료");
            }
        });

        userRepository.delete(user.getId());
        return true;
    }
}
