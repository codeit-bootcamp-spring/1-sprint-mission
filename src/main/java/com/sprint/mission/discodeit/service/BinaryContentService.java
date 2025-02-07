package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ProfileImageDTO;
import com.sprint.mission.discodeit.entity.data.BinaryContent;

public interface BinaryContentService {

    BinaryContent created(ProfileImageDTO data);
}
