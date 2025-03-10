package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final BinaryContentService binaryContentService;
  private final UserValidator userValidator;


  @Override
  public User create(UserCreateDTO dto,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    userValidator.validateUser(dto.getUsername(), dto.getEmail(), dto.getPassword());

    UUID nullableProfileId = saveBinaryFileAndReturnId(optionalProfileCreateRequest);

    User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword(), nullableProfileId);
    User saveUser = userRepository.save(user);
    userStatusRepository.save(new UserStatus(saveUser.getId()));
    return saveUser;
  }

  @Override
  public UserFindDTO find(UUID id) {
    User findUser = userRepository.findById(id);
    Optional.ofNullable(findUser)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    return toDTO(findUser);
  }

  @Override
  public List<UserFindDTO> findAll() {
    List<User> users = userRepository.findAll();
    return users.stream()
        .map(this::toDTO).toList();
  }

  @Override
  public User update(UUID id, UserUpdateDTO dto,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    userValidator.validateUpdateUser(id, dto.getNewUsername(), dto.getNewEmail(),
        dto.getNewPassword());
    User findUser = userRepository.findById(id);

    if (findUser.getProfileId() != null) {
      binaryContentRepository.delete(findUser.getProfileId());
    }

    UUID nullableProfileId = saveBinaryFileAndReturnId(optionalProfileCreateRequest);

    findUser.updateUser(dto.getNewUsername(), dto.getNewEmail(), dto.getNewPassword(),
        nullableProfileId);
    userRepository.update(findUser);
    return findUser;
  }

  @Override
  public UUID delete(UUID id) {
    User findUser = userRepository.findById(id);
    Optional.ofNullable(findUser)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    userStatusRepository.deleteByUserId(id);

    Optional.ofNullable(findUser.getProfileId())
        .ifPresent(binaryContentService::delete);

    return userRepository.delete(findUser.getId());
  }

  private UserFindDTO toDTO(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserFindDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        online,
        user.getProfileId(),
        user.getCreatedAt(),
        user.getUpdatedAt()
    );
  }

  private UUID saveBinaryFileAndReturnId(
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    return optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.getFileName();
          String contentType = profileRequest.getContentType();
          byte[] bytes = profileRequest.getBytes();
          return binaryContentRepository.save(
              new BinaryContent(bytes, fileName, contentType, (long) bytes.length)).getId();
        })
        .orElse(null);
  }

}