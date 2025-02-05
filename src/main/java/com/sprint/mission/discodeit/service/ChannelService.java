package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요

    ChannelDto createPrivateChannel(ChannelDto dto, List<UUID> userList);

    ChannelDto createPublicChannel(ChannelDto dto);

    Channel getChannel(UUID id);

    List<Channel> getChannels();

    void updateChannel(UUID id, String name);

    void deleteChannel(UUID id);
}
