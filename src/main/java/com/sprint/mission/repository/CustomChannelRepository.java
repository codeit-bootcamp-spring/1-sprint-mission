package com.sprint.mission.repository;

import com.sprint.mission.dto.response.ChannelDto;

import java.util.List;
import java.util.UUID;

public interface CustomChannelRepository {

  List<ChannelDto> findAllPrivateChannelByUserId(UUID userId);
}
