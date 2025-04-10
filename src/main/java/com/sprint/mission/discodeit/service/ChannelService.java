package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService extends CRUDService<ChannelRequest, ChannelResponse>{
    List<ChannelResponse> publicChannelReadAll();
    ChannelResponse publicChannelReadOne(UUID id);
}
