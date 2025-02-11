package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ProfileImageDTO;
import com.sprint.mission.discodeit.entity.data.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContent created(ProfileImageDTO data);

    Optional<BinaryContent> find(UUID id);

    List<BinaryContent> findAllByIdIn();

    void delete();

}
