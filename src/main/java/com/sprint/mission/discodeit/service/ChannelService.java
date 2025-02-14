package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.PublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelService extends CRUDService<ChannelRequest, ChannelResponse>{
//    ChannelResponse privateChannelCreate(PrivateChannelRequest request);
//    ChannelResponse publicChannelCreate(PublicChannelRequest request);
}
