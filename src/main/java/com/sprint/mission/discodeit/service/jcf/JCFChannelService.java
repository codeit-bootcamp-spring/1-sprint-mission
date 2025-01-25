package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final HashMap<UUID, Channel> channelData;
    private UserService userService;

    //팩토리 패턴으로 인하여 private이면 serviceFactory에서 접근이 불가하므로 public으로 변경
    public JCFChannelService(HashMap<UUID, Channel> channelData,UserService userService) {
        this.channelData = channelData;
        this.userService = userService;
    }

    @Override
    public Channel createChannel(Channel channel) {
        if (channelData.containsKey(channel.getId())) {
            throw new IllegalArgumentException("Channel Id already exists: " + channel.getId());
        }

        channelData.put(channel.getId(), channel);
        System.out.println(channel.toString());
        return channel;
    }

    @Override
    public void addParticipantToChannel(UUID channelId, UUID userId) {
        Channel channel = this.readChannel(channelId)
                .orElseThrow(() -> new IllegalArgumentException("No channel found with id: " + channelId));
        //optional로 구현했던 부분 수정
        User existUser = userService.readUser(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User does not exists: " + userId));
        channel.getParticipants().put(existUser.getId(),existUser);
        System.out.println(channel.toString());
        System.out.println(channel.getName() + " 채널에 " + existUser.getUsername() + " 유저 추가 완료\n");
    }

    @Override
    public Optional<Channel> readChannel(UUID existChannelId) {
        return Optional.ofNullable(channelData.get(existChannelId));
    }

    @Override
    public List<Channel> readAllChannels() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public Channel updateChannel(UUID ChannelId, Channel updateChannel) {
        Channel existChannel = channelData.get(ChannelId);
        // 기존 Channel 객체가 존재하는지 확인
        if (!channelData.containsKey(existChannel.getId())) {
            throw new NoSuchElementException("Channel not found");
        }
        existChannel.updateName(updateChannel.getName());
        existChannel.updateDescription(updateChannel.getDescription());
        existChannel.updateParticipants(updateChannel.getParticipants());
        existChannel.updateMessageList(updateChannel.getMessageList());
        existChannel.updateTime();
        channelData.put(existChannel.getId(), existChannel);
        return existChannel;

    }

    @Override
    public boolean deleteChannel(UUID channelId) {
        if (!channelData.containsKey(channelId)) {
            return false;
        }
        channelData.remove(channelId);
        return true;
    }

}
