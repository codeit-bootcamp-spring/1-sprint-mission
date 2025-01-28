package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;
    private final ChannelValidator channelValidator = new ChannelValidator();
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        data = new HashMap<>();
        this.userService = userService;
    }

    @Override
    public Channel createChannel(ChannelType channelType, String title, String description) {
        if (channelValidator.isValidTitle(title)) {
            Channel newChannel = new Channel(channelType, title, description);
            data.put(newChannel.getId(), newChannel);
            System.out.println("create new channel: " + newChannel.getTitle());
            return newChannel;
        }
        return null;
    }

    @Override
    public List<Channel> getAllChannelList() {
        return data.values().stream().toList();
    }

    @Override
    public Channel searchById(UUID channelId) {
        if (data.containsKey(channelId)) {
            return data.get(channelId);
        } else {
            System.out.println("Channel does not exist");
            return null;
        }
    }

    @Override
    public void updateTitle(UUID channelId, String title) {
        if (data.containsKey(channelId)) {
            if (channelValidator.isValidTitle(title)) {
                searchById(channelId).updateTitle(title);
                System.out.println("success update");
            }
        }
    }

    @Override
    public void updateDescription(UUID channelId, String description) {
        if (data.containsKey(channelId)) {
            if (channelValidator.isValidTitle(description)) {
                searchById(channelId).updateDescription(description);
                System.out.println("success update");
            }
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (data.containsKey(channelId)) {
            data.remove(channelId);
            System.out.println("success delete");
        }
    }

    // 채널의 모든 멤버
    @Override
    public List<User> getAllMemberList(UUID channelId) {
        return searchById(channelId).getMemberList();
    }

    @Override
    public void addMember(UUID channelId, UUID userId) {
        User user = userService.searchById(userId);
        Channel channel = searchById(channelId);
        if (!Objects.isNull(user) && !Objects.isNull(channel)) {
            if (channel.getMemberList().contains(user)) {
                System.out.println("user's already a channel member");
            } else {
                channel.addMember(user);
                user.addChannel(channel);
            }
        }
    }

    @Override
    public void deleteMember(UUID channelId, UUID userId) {
        User user = userService.searchById(userId);
        Channel channel = searchById(channelId);
        if (!Objects.isNull(user) && !Objects.isNull(channel)) {
            if (channel.getMemberList().contains(user)) {
                channel.removeMember(user);
                user.removeChannel(channel);
                System.out.println("success delete");
            } else {
                System.out.println("member does not exist");
            }
        }
    }
}
