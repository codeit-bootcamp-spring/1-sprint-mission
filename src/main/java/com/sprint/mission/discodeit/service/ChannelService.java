package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Dto.ChannelDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    UUID createPrivateChannel(String channelName, String description, UUID[] membersId);

    UUID createPublicChannel(String channelName, String description);

    ChannelDto findChannelById(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);

    boolean deleteChannel(UUID channelId);

    boolean changeChannelDescription(UUID channelId, String newName);

    boolean addChannelMember(UUID channelID, UUID memberID);

    String getChannelNameById(UUID userId);

    boolean printAllMemberNames(UUID channelId);


}

