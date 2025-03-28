
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// 사용자 관리 controller
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  @Override
  public ResponseEntity<UserDto> create(CreateUserRequest userRequest, MultipartFile profile) {
    return null;
  }

  @Override
  public ResponseEntity<UserDto> update(UUID userId, UpdateUserRequest userRequest,
      MultipartFile profile) {
    return null;
  }

  @Override
  public ResponseEntity<Void> delete(UUID userId) {
    return null;
  }

  @Override
  public ResponseEntity<List<UserDto>> findAll() {
    return null;
  }

  @Override
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(UUID userId,
      UpdateUserRequest request) {
    return null;
  }
}