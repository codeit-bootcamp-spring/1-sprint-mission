package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;

public interface ReadStatusService {
  ReadStatus create(CreateReadStatusDto dto, boolean skipValidation);
  List<ReadStatus> createMultipleReadStatus(List<String> userIds, String channelId);
  ReadStatus find(String id);
  List<ReadStatus> findAllByUserId(String userId);
  List<ReadStatus> findAllByChannelId(String channelId);
  ReadStatus update(CreateReadStatusDto dto);
  void deleteByChannel(String id);
  void delete(String id);
}
