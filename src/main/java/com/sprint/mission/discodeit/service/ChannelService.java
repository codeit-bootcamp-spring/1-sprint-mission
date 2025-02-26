package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;

import java.util.List;
import java.util.UUID;

public interface ChannelService extends CRUDService<ChannelRequest, ChannelResponse>{
    List<ChannelResponse> publicChannelReadAll();
    ChannelResponse publicChannelReadOne(UUID id);
}
