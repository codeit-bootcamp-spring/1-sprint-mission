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
import java.util.UUID;

public class BasicJCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private UserService userService;

    public BasicJCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
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
        Optional<Channel> existingChannel = channelRepository.findById(channelId);
        if (existingChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel does not exists: " + channelId);
        }

        Optional<User> existUser = userService.readUser(userId);
        if (existUser.isEmpty()) {
            throw new IllegalArgumentException("User does not exists: " + userId);
        }

        Channel foundChannel = existingChannel.get();
        foundChannel.getParticipants().put(userId,existUser.get());
        channelRepository.save(foundChannel);

        System.out.println(foundChannel.toString());
        System.out.println(foundChannel.getName() + " 채널에 " + existUser.get().getUsername() + " 유저 추가 완료\n");
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
        Optional<Channel> existChannel = channelRepository.findById(existChannelId);
        if (existChannel.isEmpty()) {
            throw new NoSuchElementException("Channel not found");
        }

        System.out.println("업데이트 이전 채널 = " + existChannel.toString());
        existChannel.get().updateName(updateChannel.getName());
        existChannel.get().updateDescription(updateChannel.getDescription());
        existChannel.get().updateParticipants(updateChannel.getParticipants());
        existChannel.get().updateMessageList(updateChannel.getMessageList());
        existChannel.get().updateTime();

        Channel updatedChannel = channelRepository.save(existChannel.get());
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
