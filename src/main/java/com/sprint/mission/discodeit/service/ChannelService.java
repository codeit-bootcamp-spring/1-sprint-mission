package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelFindAllDto;
import com.sprint.mission.discodeit.dto.ChannelFindDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요

    ChannelDto create(ChannelDto dto, List<UUID> userList);

    ChannelDto create(ChannelDto dto);

    ChannelFindDto find(UUID id);

    ChannelFindAllDto findAllByUserId(UUID userId);

    ChannelFindAllDto findAll();

    void updateChannel(ChannelDto dto);

    void deleteChannel(UUID id);

    UUID addMessageInChannel(MessageDto dto);

    List<UUID> findAllMessagesByChannelId(UUID channelId);
}
