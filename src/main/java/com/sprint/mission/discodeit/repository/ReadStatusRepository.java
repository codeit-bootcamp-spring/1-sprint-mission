package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;

public interface ReadStatusRepository extends BaseRepository<ReadStatus,String>{

  ReadStatus save(ReadStatus status);
  Optional<ReadStatus> findById(String id);
  List<ReadStatus> findByUserId(String userId);
  List<ReadStatus> findByChannelId(String id);
  Optional<ReadStatus> findByChannelIdAndUserId(String channelId, String userId);
  List<ReadStatus> findAll();
  void deleteByUserId(String id);
  void deleteByChannelId(String channelId);
  void clear();

}
