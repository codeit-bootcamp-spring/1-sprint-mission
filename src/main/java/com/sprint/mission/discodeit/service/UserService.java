package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto createUser(UserRequest userRequest,
      Optional<BinaryContentRequest> optionalProfileCreateRequest);

  UserDto findUserDTO(UUID id);

  List<UserDto> findAllUserDTO();

  UserDto updateUser(UUID id, UserUpdateDTO userUpdateDTO,
      Optional<BinaryContentRequest> optionalProfileCreateRequest);

  void deleteUser(UUID id);

  //user온라인상태업데이트
  UserStatusUpdateDTO updateUserStatus(UUID id, UserStatusUpdateDTO userUserStatusUpdateDTO);


}
