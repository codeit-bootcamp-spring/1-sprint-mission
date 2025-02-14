package com.sprint.mission.service;


import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.response.FindChannelDto;

import java.awt.image.VolatileImage;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    void create(ChannelDtoForRequest dto);
    List<Channel> findAll();
    Channel findById(UUID id);
    void update(UUID channelId, ChannelDtoForRequest dto);
    void delete(UUID channelId);
//    void validateDuplicateName(String name);
}