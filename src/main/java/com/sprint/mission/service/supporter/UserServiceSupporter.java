package com.sprint.mission.service.supporter;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.BinaryContent;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.BinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserServiceSupporter {

  private final BinaryContentMapper binaryContentMapper;
  private final BinaryService profileService;
  private final UserMapper userMapper;

  public void isDuplicateNameEmail(List<User> allUser, String username, String email) {
    boolean isDuplicateName = allUser.stream()
        .anyMatch(usr -> username.equals(usr.getUsername()));

    if (isDuplicateName) {
      throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);
    }

    boolean isDuplicateEmail = allUser.stream()
        .anyMatch(usr -> email.equals(usr.getEmail()));
    if (isDuplicateEmail) {
      throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
    }
  }

  public User createUser(UserDtoForCreate requestDTO, MultipartFile profile) {
    Optional<BinaryContentDtoForCreate> profileDto = binaryContentMapper.convertFileToBinaryContentDto(
        profile);
    return profileDto.map((binaryDto) -> {
      BinaryContent createdBinaryContent = profileService.create(binaryDto);
      return userMapper.toEntityWithProfile(requestDTO, createdBinaryContent);
    }).orElseGet(() -> userMapper.toEntityWithoutProfile(requestDTO));
  }
}
