package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private static final EntityFactory entityFactory = BaseEntityFactory.getInstance();

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(String channelName, User owner, Map<UUID, User> userList) {
        Channel channel = entityFactory.createChannel(channelName, owner, userList);

        channelRepository.saveChannel(channel);

        return channel;
    }

    @Override
    public Map<UUID, Channel> getChannelByName(String channelName) {
        Map<UUID, Channel> allChannels = getAllChannels();

        return allChannels.entrySet().stream()
                .filter(entry -> entry.getValue().getChannelName().equals(channelName))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channelRepository.findChannelById(channelId);
    }

    @Override
    public Map<UUID, Channel> getAllChannels() {
        return channelRepository.findAllChannel();
    }

    @Override
    public Channel updateChannel(UUID channelUUID, String channelName, User changeUser) {
        Channel findChannel = channelRepository.findChannelById(channelUUID);

        findChannel.updateChannelName(channelName);
        findChannel.updateOwnerUser(changeUser);

        return channelRepository.saveChannel(findChannel);
    }

    @Override
    public void removeChannelById(UUID channelUUID) {
        channelRepository.removeChannelById(channelUUID);
    }

    @Override
    public void addUserChannel(UUID channelUUID, User addUser) {
        Map<UUID, Channel> allChannel = channelRepository.findAllChannel();

        Channel addUserChannel = allChannel.get(channelUUID);
        addUserChannel.addUser(addUser);

        channelRepository.saveChannel(addUserChannel);
    }

    @Override
    public void kickUserChannel(UUID channelUUID, User kickUser) {
        Channel findChannel = channelRepository.findChannelById(channelUUID);

        findChannel.removeUser(kickUser);

        channelRepository.saveChannel(findChannel);
    }
}
