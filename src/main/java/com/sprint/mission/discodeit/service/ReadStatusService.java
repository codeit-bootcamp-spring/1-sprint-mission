package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;

import java.util.List;

public interface ReadStatusService {

  ReadStatusDto create(CreateReadStatusDto createReadStatusDto);

  ReadStatusDto findById(String userStatusId);

  List<ReadStatusDto> findAllByUserId(String userId);

  ReadStatusDto update(String id, UpdateReadStatusDto updateReadStatusDto);
  
  boolean delete(String userStatusId);
}
