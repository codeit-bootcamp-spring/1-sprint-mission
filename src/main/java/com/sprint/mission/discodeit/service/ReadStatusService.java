package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {


    UUID create(String username, String email, String password);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId();
    ReadStatus update(UUID id, String name, String email);
    UUID delete(UUID id);
}
