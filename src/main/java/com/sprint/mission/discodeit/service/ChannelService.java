package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChannelService {

    Channel createChannel(Channel channel);
    Optional<Channel> getChannelById(String channelId);
    List<Channel> getAllChannels();
    List<Channel> getChannelsByCategory(String categoryId);
    void updateChannel(String channelId, ChannelUpdateDto updatedChannel);
    void deleteChannel(String channelId);
    String generateInviteCode(Channel channel);
    void setPrivate(Channel channel);
    void setPublic(Channel channel);



}
