package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private UserService userService;

    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
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
    public void addParticipantToChannel(UUID channelId, UUID userId) {
        Channel existChannel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + channelId));

        User existUser = userService.readUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist: " + userId));

        existChannel.getParticipants().put(userId,existUser);
        channelRepository.save(existChannel);

        System.out.println(existChannel.toString());
        System.out.println(existChannel.getName() + " 채널에 " + existUser.getUsername() + " 유저 추가 완료\n");
    }

    @Override
    public Optional<Channel> readChannel(UUID existChannelId) {
        Optional<Channel> foundChannel = channelRepository.findById(existChannelId);
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
    public Channel updateChannel(UUID existChannelId, Channel updateChannel) {
        Channel existChannel = channelRepository.findById(existChannelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + existChannelId));

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
    public boolean deleteChannel(UUID channelId) {
        Optional<Channel> channelOptional = channelRepository.findById(channelId);
        if (channelOptional.isEmpty()) {
            return false;
        }
        channelRepository.delete(channelId);
        return true;
    }
}
