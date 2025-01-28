package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(ChannelType channelType, String title, String description);

    List<Channel> getAllChannelList();

    Channel searchById(UUID channelId);

    void updateTitle(UUID channelId, String title);

    void updateDescription(UUID channelId, String description);

    void deleteChannel(UUID channelId);

    void addMember(UUID channelId, UUID userId);

    void deleteMember(UUID channelId, UUID userId);

    List<User> getAllMemberList(UUID channelId);

}
