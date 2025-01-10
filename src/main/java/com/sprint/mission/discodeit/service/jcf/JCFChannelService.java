package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> channelList;
    private final EntityFactory entityFactory;

    public JCFChannelService(EntityFactory entityFactory ,List<Channel> channelList) {
        this.entityFactory = entityFactory;
        this.channelList = new ArrayList<>(channelList);
    }

    public JCFChannelService() {
        this.entityFactory = new BaseEntityFactory();
        channelList = new ArrayList<>();
    }

    @Override
    public Channel createChannel(String channelName, User owner, List<User> userList) {
        Channel channel = entityFactory.createChannel(channelName, owner, userList);
        channelList.add(channel);
        return channel;
    }

    @Override
    public Channel getChannelByName(String channelName) {
        return channelList.stream()
                .filter(readChannel -> readChannel.getChannelName().equals(channelName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("찾는 채널이 없습니다."));
    }

    @Override
    public Channel findChannelById(UUID id) {
        return channelList.stream()
                .filter(ch -> ch.getChannelId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("찾는 채널이 없어요."));
    }


    @Override
    public List<Channel> getAllChannels() {
        return channelList.stream()
                .toList();
    }

    @Override
    public Channel updateChannel(Channel channelToUpdate) {
        Channel exChannel = findChannelById(channelToUpdate.getChannelId());
        exChannel.updateChannelName(channelToUpdate.getChannelName());
        exChannel.updateOwnerUser(channelToUpdate.getChannelOwnerUser());
        exChannel.getChannelUsers().addAll(channelToUpdate.getChannelUsers());

        return exChannel;
    }

    @Override
    public void removeChannel(String removeChannelName) {
        Channel findChannel = getChannelByName(removeChannelName);
        channelList.remove(findChannel);
    }

    @Override
    public void kickUserChannel(String channelName, User kickUser) {
        Channel findChannel = getChannelByName(channelName);
        List<User> userList = findChannel.getChannelUsers();

        if(!userList.contains(kickUser) || findChannel.getChannelUsers().isEmpty()) {
            throw new IllegalArgumentException("강퇴할 유저가 없습니다.");
        }

        findChannel.removeUser(kickUser);

        if (findChannel.getChannelOwnerUser().equals(kickUser)) {
            findNextOwnerUser(findChannel);
        }
    }
    private void findNextOwnerUser(Channel findChannel) {
        User nextOwnerUser = findChannel.getChannelUsers().stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("채널에 아무도 없습니다."));
        findChannel.updateOwnerUser(nextOwnerUser);
    }
}
