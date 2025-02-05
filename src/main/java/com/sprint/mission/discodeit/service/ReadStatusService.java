package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;

public interface ReadStatusService {

    ReadStatus findById(String userStatusId);

    List<ReadStatus> findAll();

    boolean update(ReadStatus userStatus);

    boolean delete(String userStatusId);
}
