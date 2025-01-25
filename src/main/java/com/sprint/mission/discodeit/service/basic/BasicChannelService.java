package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelValidator channelValidator;
    private final UserService userService;

    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelValidator = new ChannelValidator();
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public Channel createChannel(ChannelType channelType, String title, String description) {
        if (channelValidator.isValidTitle(title)) {
            Channel newChannel = new Channel(channelType, title, description);
            channelRepository.save(newChannel);
            System.out.println("create channel: " + newChannel.getTitle());
            return newChannel;
        }
        return null;
    }

    @Override
    public List<Channel> getAllChannelList() {
        return channelRepository.findAll();
    }

    @Override
    public Channel searchById(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel does not exist"));
    }

    @Override
    public void updateTitle(UUID channelId, String title) {
        Channel channel = searchById(channelId);
        if (channelValidator.isValidTitle(title)) {
            channel.updateTitle(title);
            channelRepository.save(channel);
            System.out.println("success update");
        }
    }

    @Override
    public void updateDescription(UUID channelId, String description) {
        Channel channel = searchById(channelId);
        if (channelValidator.isValidTitle(description)) {
            channel.updateDescription(description);
            channelRepository.save(channel);
            System.out.println("success update");
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.delete(channelId);
    }

    @Override
    public List<User> getAllMemberList(UUID channelId) {
        Channel channel = searchById(channelId);
        return channel.getMemberList();
    }

    @Override
    public void addMember(UUID channelId, UUID userId) {
        User user = userService.searchById(userId);
        Channel channel = searchById(channelId);
        if (channel.getMemberList().contains(user)) {
            System.out.println("user's already a channel member");
        } else {
            channel.addMember(user);
            user.addChannel(channel);
            channelRepository.save(channel);
        }
    }

    @Override
    public void deleteMember(UUID channelId, UUID userId) {
        User user = userService.searchById(userId);
        Channel channel = searchById(channelId);
        if (channel.getMemberList().contains(user)) {
            channel.removeMember(user);
            user.removeChannel(channel);
            System.out.println("success delete");
            channelRepository.save(channel);
        } else {
            System.out.println("member does not exist");
        }
    }

}
