package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserCreationFacade {
  CreateUserResponse createUser(CreateUserRequest userDto, MultipartFile profile);
}
