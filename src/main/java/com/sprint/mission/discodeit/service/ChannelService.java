package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannelPublic(ChannelDTO channelDTO);
    Channel createChannelPrivate(ChannelDTO channelDTO);
    Channel findById(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, ChannelDTO channelDTO);
    void addUserInChannel(UUID channelId, UUID userId); // 유저 채널에 등록
    void delete(UUID channelId);
    void deleteUserInChannel(UUID channelId, UUID userId);
    void deleteUserInAllChannel(UUID userId); //유저 삭제 시 -> 전체에서 삭제 처리
}
