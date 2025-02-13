package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;

public interface ReadStatusRepository {

    ReadStatus save(ReadStatus readStatus);

    ReadStatus findById(String id);

    List<ReadStatus> findAll();

    boolean delete(String id);

    ReadStatus findByChannelIdWithUserId(String channelId, String userId);

    List<ReadStatus> findAllByUserId(String userId);

    List<ReadStatus> findAllByChannelId(String channelId);

}
