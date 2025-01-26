package mission.controller;

import mission.entity.Channel;

import java.util.Set;
import java.util.UUID;

public interface ChannelController {
    // 채널명은 중복 허용 X
    Channel create(String name);
    Channel updateChannelName(UUID channelId, String newName);
    Channel findById(UUID id);
    Set<Channel> findAll();
    Channel findChannelByName(String channelName);
    Set<Channel> findAllChannelByUser(UUID userId);
    void delete(UUID channelId);
    void addUserByChannel(UUID channelId, UUID userId);
    void drops(UUID channel_Id, UUID droppingUser_Id);

    void createChannelDirectory();
}
