package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Type.ChannelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    UUID createPrivateChannel(String channelName, String description, UUID[] membersId);

    UUID createPublicChannel(String channelName, String description);

    ChannelDto findChannelById(UUID channelId);

    boolean deleteChannel(UUID channelId);

    boolean changeChannelName(UUID channelId, String newName);

    boolean addChannelMember(UUID channelID, UUID memberID);

    String getChannelNameById(UUID userId);

    boolean printAllMemberNames(UUID channelId);


}

