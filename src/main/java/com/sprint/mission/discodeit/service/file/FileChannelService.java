package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService, FileService<Channel> {
    private static final String CHANNEL_PATH = "channel.ser";

    static EntityFactory ef;
    private Map<UUID, Channel> channelList = new HashMap<>();

    public FileChannelService(EntityFactory ef) {
        FileChannelService.ef = ef;
    }

    public FileChannelService() {
        ef = BaseEntityFactory.getInstance();
    }

    @Override
    public Channel createChannel(String channelName, User owner, Map<UUID, User> userList) {
        Channel channel = ef.createChannel(channelName, owner, userList);

        channelList.put(channel.getChannelId(), channel);

        save(CHANNEL_PATH, channelList);

        return channel;
    }

    @Override
    public Map<UUID, Channel> getChannelByName(String channelName) {
        return channelList.entrySet().stream()
                .filter(entry -> entry.getValue().getChannelName().equals(channelName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channelList.get(channelId);
    }

    @Override
    public Map<UUID, Channel> getAllChannels() {
        return load(CHANNEL_PATH, channelList);
    }

    @Override
    public Channel updateChannel(UUID channelUUID, String channelName, User changeUser) {
        Channel findChannel = findChannelById(channelUUID);

        findChannel.updateChannelName(channelName);
        findChannel.updateOwnerUser(changeUser);

        save(CHANNEL_PATH, channelList);

        return findChannel;
    }

    @Override
    public void removeChannelById(UUID channelUUID) {
        channelList.remove(channelUUID);

        save(CHANNEL_PATH, channelList);
    }

    @Override
    public void addUserChannel(UUID channelUUID, User addUser) {
        Map<UUID, Channel> load = load(CHANNEL_PATH, channelList);

        Channel findChannel = findChannelById(channelUUID);
        findChannel.addUser(addUser);

        save(CHANNEL_PATH, channelList);
    }

    @Override
    public void kickUserChannel(UUID channelUUID, User kickUser) {
        Channel findChannel = findChannelById(channelUUID);

        findChannel.removeUser(kickUser);

        if (findChannel.getChannelOwnerUser().equals(kickUser)) {
            findNextOwnerUser(findChannel);
        }

        save(CHANNEL_PATH, channelList);
    }

    private void findNextOwnerUser(Channel findChannel) {
        User nextOwnerUser = findChannel.getChannelUsers().entrySet().stream()
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("채널에 아무도 없습니다."));

        findChannel.updateOwnerUser(nextOwnerUser);
    }

}
