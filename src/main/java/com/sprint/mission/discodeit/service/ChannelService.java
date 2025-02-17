package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;

import java.util.List;

public interface ChannelService extends CRUDService<ChannelRequest, ChannelResponse>{
    List<ChannelResponse> publicChannelReadAll();
//    ChannelResponse privateChannelCreate(PrivateChannelRequest request);
//    ChannelResponse publicChannelCreate(PublicChannelRequest request);
}
