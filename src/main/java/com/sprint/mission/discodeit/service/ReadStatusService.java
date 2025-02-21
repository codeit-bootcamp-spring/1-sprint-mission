package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Map;

public interface ReadStatusService {
  ReadStatus create(CreateReadStatusDto dto, boolean skipValidation);
  List<ReadStatus> createMultipleReadStatus(List<String> userIds, String channelId);
  ReadStatus find(String id);
  List<ReadStatus> findAllByUserId(String userId);
  ReadStatus updateById(UpdateReadStatusDto readStatusDto, String id);
  List<ReadStatus> findAllByChannelId(String channelId);
  Map<String,List<String>> getUserIdsForChannelReadStatuses(List<Channel> channels);
  ReadStatus update(CreateReadStatusDto dto);
  void deleteByChannel(String id);
  void delete(String id);
}
