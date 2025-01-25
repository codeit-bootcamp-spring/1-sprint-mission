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
    public void addParticipantToChannel(Channel channel, User user) {
        if(!channelData.containsKey(channel.getId())) {
            throw new IllegalArgumentException("Channel does not exists: " + channel.getId());
        }
        //optional로 구현했던 부분 수정
        User existUser = userService.readUser(user)
                        .orElseThrow(() -> new IllegalArgumentException("User does not exists: " + user.getId()));
        channel.getParticipants().put(existUser.getId(),existUser);
        System.out.println(channel.toString());
        System.out.println(channel.getName() + " 채널에 " + user.getUsername() + " 유저 추가 완료\n");
    }

    @Override
    public Optional<Channel> readChannel(UUID channelId) {
        return Optional.ofNullable(channelData.get(channelId));
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
    public boolean deleteChannel(Channel channel) {
        if (!channelData.containsKey(channel.getId())) {
            return false;
        }
        channelData.remove(channel.getId());
        return true;
    }

}
