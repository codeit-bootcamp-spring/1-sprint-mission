package com.sprint.mission.discodeit.service.basic.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BasicJCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private UserService userService;
    private MessageService messageService;

    public BasicJCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void setDependencies(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public Channel createChannel(Channel channel) {
        Optional<Channel> existingChannel = channelRepository.findById(channel.getId());
        if (existingChannel.isPresent()) {
            throw new IllegalArgumentException("Channel Id already exists: " + channel.getId());
        }

        Channel savedChannel = channelRepository.save(channel);
        System.out.println(savedChannel.toString());
        return savedChannel;
    }

    @Override
    public void addParticipantToChannel(Channel channel, User user) {
        Optional<Channel> existingChannel = channelRepository.findById(channel.getId());
        if (existingChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel does not exists: " + channel.getId());
        }

        Optional<User> existUser = userService.readUser(user);
        if (existUser.isEmpty()) {
            throw new IllegalArgumentException("User does not exists: " + user.getUsername());
        }

        Channel foundChannel = existingChannel.get();
        foundChannel.getParticipants().add(existUser.get());
        channelRepository.save(foundChannel);

        System.out.println(foundChannel.toString());
        System.out.println(foundChannel.getName() + " 채널에 " + user.getUsername() + " 유저 추가 완료\n");
    }

    @Override
    public Optional<Channel> readChannel(Channel channel) {
        Optional<Channel> foundChannel = channelRepository.findById(channel.getId());
        if (foundChannel.isPresent()) {
            System.out.println(foundChannel.get().toString());
        }
        return foundChannel;
    }

    @Override
    public List<Channel> readAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Channel updateChannel(Channel existChannel, Channel updateChannel) {
        Optional<Channel> channelOptional = channelRepository.findById(existChannel.getId());
        if (channelOptional.isEmpty()) {
            throw new NoSuchElementException("Channel not found");
        }

        System.out.println("업데이트 이전 채널 = " + existChannel.toString());
        existChannel.updateName(updateChannel.getName());
        existChannel.updateDescription(updateChannel.getDescription());
        existChannel.updateParticipants(updateChannel.getParticipants());
        existChannel.updateMessageList(updateChannel.getMessageList());
        existChannel.updateTime();

        Channel updatedChannel = channelRepository.save(existChannel);
        System.out.println("업데이트 이후 채널 = " + updatedChannel.toString());
        return updatedChannel;
    }

    @Override
    public boolean deleteChannel(Channel channel) {
        Optional<Channel> channelOptional = channelRepository.findById(channel.getId());
        if (channelOptional.isEmpty()) {
            return false;
        }

        messageService.deleteMessageByChannel(channel);
        channelRepository.delete(channel.getId());
        return true;
    }
}
