package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    UUID create(String username, String email, String password);
    BinaryContent find(UUID id);
    List<BinaryContent> findAllByIdIn();
    UUID delete(UUID id);

}
