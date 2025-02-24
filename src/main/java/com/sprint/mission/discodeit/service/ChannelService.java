package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.DeleteChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.FindChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(CreatePublicChannelRequest createPublicChannelRequest);

    Channel createPrivateChannel(CreatePrivateChannelRequest createPrivateChannelRequest);

    FindChannelResponse findChannelByIdOrThrow(UUID channelId);

    List<FindChannelResponse> findAllChannelsByUserId(UUID userId);

    Channel updateChannel(UUID channelId, UpdateChannelRequest updateChannelRequest);

    void deleteChannel(UUID channelId, DeleteChannelRequest deleteChannelRequest);

    Channel inviteUsers(UUID channelId, List<User> invitedUsers);

    Channel leaveUsers(UUID channelId, List<User> leaveUsers);
}
