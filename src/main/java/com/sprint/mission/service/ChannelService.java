package com.sprint.mission.service;


import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.dto.request.ChannelDtoForRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(ChannelDtoForRequest dto);

    List<Channel> findAllByUserId(UUID userId);

    List<Channel> findAll();
    Channel findById(UUID id);
    void update(UUID channelId, ChannelDtoForRequest dto);
    void delete(UUID channelId);
//    void validateDuplicateName(String name);
}