package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.UUID;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Named("UserMapperHelper")
public class UserMapperHelper {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final UserStatusRepository userStatusRepository;

  @Autowired
  public UserMapperHelper(BinaryContentRepository binaryContentRepository,
      BinaryContentMapper binaryContentMapper,
      UserStatusRepository userStatusRepository) {
    this.binaryContentRepository = binaryContentRepository;
    this.binaryContentMapper = binaryContentMapper;
    this.userStatusRepository = userStatusRepository;
  }

  @Named("mapProfileById")
  public BinaryContentDto mapProfileById(UUID profileId) {
    return binaryContentRepository.findById(profileId)
        .map(binaryContentMapper::toDto)
        .orElse(null);
  }

  @Named("mapOnline")
  public Boolean mapOnline(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .map(UserStatus::isOnline)
        .orElse(null);
  }
}