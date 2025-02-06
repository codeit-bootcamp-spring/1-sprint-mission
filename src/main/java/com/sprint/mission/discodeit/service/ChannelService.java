package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface ChannelService {
    UUID createChannel(ChannelType type, String channelName);

    HashMap<UUID, Channel> getChannelsMap();

    Channel getChannelById(UUID channelId);

    boolean deleteChannel(UUID channelId);

    boolean changeChannelName(UUID channelId, String newName);

    boolean addChannelMember(UUID channelID, UUID memberID);

    boolean isChannelExist(UUID channelId);

    ArrayList<User> getAllMembers(UUID channelId);

    String getChannelNameById(UUID userId);

    boolean printAllMemberNames(UUID channelId);


}

