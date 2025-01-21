package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelList;
    private static EntityFactory entityFactory;

    public JCFChannelService() {
        channelList = new HashMap<>();
    }

    public JCFChannelService(EntityFactory entityFactory) {
        JCFChannelService.entityFactory = entityFactory;
        this.channelList = new HashMap<>();
    }

    @Override
    public Channel createChannel(String channelName, User owner, Map<UUID, User> userList) {
        Channel channel = entityFactory.createChannel(channelName, owner, userList);
        channelList.put(channel.getChannelId(), channel);
        return channel;
    }

    @Override
    public Map<UUID, Channel> getChannelByName(String channelName) {
        return channelList.entrySet().stream()
                .filter(entry -> entry.getValue().getChannelName().equals(channelName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    @Override
    public Channel findChannelById(UUID id) {
        return channelList.entrySet().stream()
                .filter(entry -> entry.getValue().getChannelId().equals(id))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("찾는 채널이 없습니다"));
    }


    @Override
    public Map<UUID, Channel> getAllChannels() {
        return channelList;
    }

    @Override
    public Channel updateChannel(UUID channelUUID, String channelName, User changeOwnerUser) {
        Channel findChannel = findChannelById(channelUUID);

        findChannel.updateChannelName(channelName);
        findChannel.updateOwnerUser(changeOwnerUser);

        return findChannel;
    }

    @Override
    public void removeChannelById(UUID removeChannelUUID) {
        Channel channelById = findChannelById(removeChannelUUID);

        channelList.remove(channelById.getChannelId());
    }

    @Override
    public void addUserChannel(UUID channelUUID, User addUser) {
        Channel channelById = findChannelById(channelUUID);

        channelById.addUser(addUser); //
    }

    @Override
    public void kickUserChannel(UUID channelUUID, User kickUser) {
        Channel findChannel = findChannelById(channelUUID);

        User findKickUser = channelList.get(findChannel.getChannelId())
                .getChannelUsers().get(kickUser.getUserId());

        findChannel.removeUser(findKickUser);

        if (findChannel.getChannelOwnerUser().equals(findKickUser)) {
            findNextOwnerUser(findChannel);
        }
    }

    private void findNextOwnerUser(Channel findChannel) {
        User nextOwnerUser = findChannel.getChannelUsers().entrySet().stream()
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("채널에 아무도 없습니다."));

        findChannel.updateOwnerUser(nextOwnerUser);
    }
}
