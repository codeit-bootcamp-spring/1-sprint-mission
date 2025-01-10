package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;
    private final UserService userService;
    private final MessageService messageService;

    public JCFChannelService(Map<UUID, Channel> data, UserService userService, MessageService messageService) {
        this.data = data;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public Channel createChannel(Channel channel) {
        if (data.containsKey(channel.getId())) {
            throw new IllegalArgumentException("Channel Id already exists: " + channel.getId());
        }

        data.put(channel.getId(), channel);
        System.out.println(channel.toString());
        return channel;
    }

    @Override
    public void addParticipantToChannel(Channel channel, User user) {
        if(!data.containsKey(channel.getId())) {
            throw new IllegalArgumentException("Channel does not exists: " + channel.getId());
        }
        Optional<User> existUser = userService.readUser(user);
        if (existUser.isEmpty()) {
            throw new IllegalArgumentException("User does not exists: " + user.getUsername());
        }
        channel.getParticipants().add(existUser.get());
        System.out.println(channel.toString());
        System.out.println(channel.getName()+" 채널에 "+ user.getUsername() + " 유저 추가 완료\n");
    }
    @Override
    public Optional<Channel> readChannel(Channel channel) {
        System.out.println(channel.toString());
        return Optional.ofNullable(data.get(channel.getId()));
    }

    @Override
    public List<Channel> readAllChannels() {

        return new ArrayList<>(data.values());
    }

    @Override
    public Channel updateChannel(Channel existChannel, Channel updateChannel) {
        // 기존 Channel 객체가 존재하는지 확인
        if (!data.containsKey(existChannel.getId())) {
            throw new NoSuchElementException("Channel not found");
        }
        System.out.println("업데이트 이전 채널 = "+existChannel.toString());
        existChannel.updateName(updateChannel.getName());
        existChannel.updateDescription(updateChannel.getDescription());
        existChannel.updateParticipants(updateChannel.getParticipants());
        existChannel.updateMessageList(updateChannel.getMessageList());
        existChannel.updateTime();
        data.put(existChannel.getId(), existChannel);
        System.out.println("업데이트 이후 채널 = "+existChannel.toString());
        return existChannel;

    }

    @Override
    public boolean deleteChannel(Channel channel) {
        if (!data.containsKey(channel.getId())) {
            return false;
        }

        //채널 삭제 시 관련 메시지들 모두 삭제
        messageService.deleteMessageByChannel(channel);
        data.remove(channel.getId());
        return true;
    }

}
