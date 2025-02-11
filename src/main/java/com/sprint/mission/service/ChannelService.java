package com.sprint.mission.service;


import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.service.dto.request.ChannelDtoForRequest;
import com.sprint.mission.service.dto.response.FindChannelDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(ChannelDtoForRequest dto);
    List<FindChannelDto> findAll();
    FindChannelDto findById(UUID id);
    void update(ChannelDtoForRequest dto);
    void delete(UUID channelId);
//    void validateDuplicateName(String name);
}