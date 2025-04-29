package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto createUser(UserRequest userRequest,
      Optional<BinaryContentRequest> optionalProfileCreateRequest);

  UserDto findUserDTO(UUID id);

  List<UserDto> findAllUserDTO();

  UserDto updateUser(UUID id, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentRequest> optionalProfileCreateRequest);

  void deleteUser(UUID id);

  //user온라인상태업데이트
  UserStatusUpdateRequest updateUserStatus(UUID id,
      UserStatusUpdateRequest userUserStatusUpdateRequest);


}
