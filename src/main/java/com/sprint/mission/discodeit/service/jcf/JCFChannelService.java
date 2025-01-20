package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID,Channel> data;
    private final ChannelValidator channelValidator = new ChannelValidator();
    // 유저 서비스 가지고 와야 함. - 의존성 주입
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        data = new HashMap<>();
        this.userService = userService;
    }

    @Override
    public Channel createChannel(String title, String description, UUID userId) {
        User getUser = userService.searchById(userId);
        if (!Objects.isNull(getUser) && channelValidator.isValidTitle(title)) {
            Channel newChannel = new Channel(title, description, getUser);
            data.put(newChannel.getId(),newChannel);
            System.out.println(getUser.getName() + "create new channel");
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
    public void deleteChannel(UUID channelId) {
        if (data.containsKey(channelId)) {
            data.remove(channelId);
            System.out.println("success delete");
        }
    }

    // 채널의 모든 멤버
    @Override
    public List<User> getAllMemberList(Channel channel) {
        return channel.getMemberList();
    }

    @Override
    public void addMember(Channel channel, User user) {
        // 이미 멤버가 있는지 확인 후 추가
        if (channel.getMemberList().contains(user)) {
            System.out.println("user's already a channel member");
        } else {
            channel.addMember(user);
            user.addChannel(channel);
        }
    }

    @Override
    public void deleteMember(Channel channel, User user) {
        if (channel.getMemberList().contains(user)) {
            channel.removeMember(user);
            user.removeChannel(channel);
            System.out.println("success delete");
        } else {
            System.out.println("member does not exist");
        }
    }
}
