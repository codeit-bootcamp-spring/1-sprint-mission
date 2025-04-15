package com.sprint.mission.repository;

import com.sprint.mission.dto.response.PrivateChannelWithUserAndLastMessageAtDto;
import java.util.List;
import java.util.UUID;

public interface CustomChannelRepository {
    List<PrivateChannelWithUserAndLastMessageAtDto> findAllPrivateChannelByUserId(UUID userId);
}
