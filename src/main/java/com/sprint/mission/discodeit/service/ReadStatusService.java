package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;

public interface ReadStatusService {

    ReadStatusResponseDto create(CreateReadStatusDto createReadStatusDto);

    List<ReadStatusResponseDto> createByChannelId(String channelId);

    ReadStatusResponseDto findById(String userStatusId);

    List<ReadStatusResponseDto> findAllByUserId(String userId);

    List<ReadStatusResponseDto> findAllByChannelId(String channelId);

    ReadStatusResponseDto update(String id, UpdateReadStatusDto updateReadStatusDto);

    List<ReadStatusResponseDto> updateByUserId(String userId, UpdateReadStatusDto updateReadStatusDto);

    List<ReadStatusResponseDto> updateByChannelId(String channelId, UpdateReadStatusDto updateReadStatusDto);


    boolean delete(String userStatusId);
}
