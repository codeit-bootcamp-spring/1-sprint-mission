package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Type.ChannelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface ChannelService {
    UUID createChannel(ChannelType type, String channelName, String description);

    Channel getChannelById(UUID channelId);

    boolean deleteChannel(UUID channelId);

    boolean changeChannelName(UUID channelId, String newName);

    boolean addChannelMember(UUID channelID, UUID memberID);

    boolean isChannelExist(UUID channelId);

    ArrayList<UUID> getAllMembers(UUID channelId);

    String getChannelNameById(UUID userId);

    boolean printAllMemberNames(UUID channelId);


}

