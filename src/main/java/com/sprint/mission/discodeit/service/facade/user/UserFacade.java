package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface UserFacade {

  UserDto createUser(CreateUserRequest request, MultipartFile profile);

  UserDto updateUser(String userId, MultipartFile profile, UserUpdateDto updateDto,
      UserDetails userDetails);

  UserDto findUserById(String id);

  List<UserDto> findAllUsers();

  void deleteUser(String id);
}
