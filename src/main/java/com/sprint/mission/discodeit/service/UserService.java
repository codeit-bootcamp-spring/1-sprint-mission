package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.request.UserRequest.Update;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

  UserResponse createUser(UserRequest.Create request, MultipartFile userProfileImage);

  List<UserResponse> findAll();

  UserResponse findById(UUID id);

  UserResponse update(UUID id, UserRequest.Update request, MultipartFile userProfileImage);

  void deleteById(UUID id);
}
