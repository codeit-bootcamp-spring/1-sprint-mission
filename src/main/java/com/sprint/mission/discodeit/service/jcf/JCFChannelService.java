package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.Factory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;


import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.error.ChannelError.*;
import static com.sprint.mission.discodeit.error.UserError.CANNOT_FOUND_USER;


public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelRepository;
    private final UserService userService;
    private final MessageService messageService;

    public JCFChannelService() {
        this.channelRepository = new HashMap<>();
        this.userService = Factory.getUserService();
        this.messageService = Factory.getMessageService();
    }

    @Override
    public Channel createChannel(Channel createChannel) throws IllegalArgumentException {
        for (Channel channel : channelRepository.values()) { // 채널 이름이 중복되면 안 됨
            if (channel.getName() == createChannel.getName()) {
                throw new IllegalArgumentException(DUPLICATE_NAME.getMessage());
            }
        }
//        if (userService.getUserByPhone(createChannel.getCreator().getPhone()) == null) {
//            throw new IllegalArgumentException(CANNOT_FOUND_USER.getMessage());
//        }

        channelRepository.put(createChannel.getId(), createChannel);
        return createChannel;
    }

    @Override
    public Channel getChannelByName(String name) {
        for (Channel channel : channelRepository.values()) {
            if (channel.getName() == name) {
                return channel;
            }
        }
        throw new IllegalArgumentException(CANNOT_FOUND_NAME.getMessage());
    }

    @Override
    public boolean channelExist(UUID uuid) {
        return channelRepository.containsKey(uuid);
    }

    @Override
    public List<Channel> getAllChannel() {
        return channelRepository.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getChannelsByUserId(User user) {
//        if (!userService.userExists(user.getId())) {
//            throw new IllegalArgumentException(CANNOT_FOUND_USER.getMessage());
//        }
        List<Channel> channels = new ArrayList<>();
        for (Channel channel : channelRepository.values()) {
            if (channel.getMembers().contains(user)) {
                channels.add(channel);
            }
        }
        return channels;
    }

    @Override
    public Channel addUserToChannel(Channel channel, User newUser) {//새로운 유저가 채널에 들어갈때
        if (!channelRepository.containsKey(channel.getId())) {
            throw new IllegalArgumentException(CANNOT_FOUND_CHANNEL.getMessage());
        }
        if (userService.userExists(newUser.getId())) {
            throw new IllegalArgumentException(CANNOT_FOUND_USER.getMessage());
        }
        channel.addUser(newUser);
        return channel;
    }

    @Override
    public Channel addManyUserToChannel(Channel channel, List<User> users) {
        if (!channelRepository.containsKey(channel.getId())) {
            throw new IllegalArgumentException(CANNOT_FOUND_CHANNEL.getMessage());
        }
        channel.addManyUser(users);
        return channel;
    }

    @Override
    public Channel removeUserToChannel(Channel channel, User removeUser) {
        if (!channelRepository.containsKey(channel.getId())) {
            throw new IllegalArgumentException(CANNOT_FOUND_CHANNEL.getMessage());
        }

        if (userService.userExists(removeUser.getId())) {
            throw new IllegalArgumentException(CANNOT_FOUND_USER.getMessage());
        }
        channel.removeUser(removeUser);
        return channel;
    }

    @Override
    public void deleteChannel(Channel channel) { // 채널이 사라지면 해당 채널에 포함된 메시지도 사라진다.
        if (!channelRepository.containsKey(channel.getId())) {
            throw new IllegalArgumentException(CANNOT_FOUND_CHANNEL.getMessage());
        }

        messageService.deleteMessageWithChannel(channel);
        channelRepository.remove(channel.getId());
    }
}