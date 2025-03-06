package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.interfacepac.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserStatusRepository;
import com.sprint.mission.discodeit.service.interfacepac.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;


  public BasicUserService(UserRepository userRepository,
      BinaryContentRepository binaryContentRepository,
      UserStatusRepository userStatusRepository) {
    this.userRepository = userRepository;
    this.binaryContentRepository = binaryContentRepository;
    this.userStatusRepository = userStatusRepository;
  }

  @Override
  public UserResponseDTO create(UserCreateDTO userCreateDTO,
      BinaryContentCreateRequest binaryContentCreateRequest) {
    if (userRepository.existsByEmail(userCreateDTO.email())) {
      throw new UserAlreadyExistsException(
          "User email " + userCreateDTO.email() + " already exists");
    }
    if (userRepository.existsByUsername(userCreateDTO.username())) {
      throw new UserAlreadyExistsException(
          "UserName " + userCreateDTO.username() + " already exists");
    }
    // 새 사용자 생성, 저장
    User newUser = new User(userCreateDTO.username(), userCreateDTO.email(),
        userCreateDTO.password());
    userRepository.save(newUser);

    // 프로필 이미지 저장 (선택적)
    if (binaryContentCreateRequest != null &&
        binaryContentCreateRequest.bytes() != null
        && binaryContentCreateRequest.bytes().length > 0) {
      BinaryContent binaryContent = new BinaryContent(
          UUID.randomUUID(),
          newUser.getId(),
          null,
          binaryContentCreateRequest.fileName(),
          binaryContentCreateRequest.contentType(),
          binaryContentCreateRequest.bytes()

      );
      BinaryContent profileContent = binaryContentRepository.save(binaryContent);
    }
    //사용자 상태 생성, 저장
    UserStatus userStatus = new UserStatus(newUser, null);
    userStatusRepository.save(userStatus);

    return UserResponseDTO.fromEntity(newUser, userStatus,
        binaryContentRepository.findByUserId(newUser.getId()));
  }

  @Override
  public UserResponseDTO find(UUID userId) {
    //사용자 찾고
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    //사용자 상태 확인
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElse(null);

    //변환 반환
    return UserResponseDTO.fromEntity(user, userStatus,
        binaryContentRepository.findByUserId(user.getId()));
  }

  @Override
  public List<UserResponseDTO> findAll() {
    return userRepository.findAll().stream()
        .map(user -> {
          UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElse(null);
          return UserResponseDTO.fromEntity(user, userStatus,
              binaryContentRepository.findByUserId(user.getId()));
        })
        .toList();
  }

  @Override
  public UserResponseDTO update(UUID userId, UserUpdateDTO userUpdateDTO,
      BinaryContentCreateRequest binaryContentCreateRequest) {
    //사용자 찾고
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

    //중복체크
    if (!user.getEmail().equals(userUpdateDTO.newEmail()) &&
        userRepository.existsByEmail(userUpdateDTO.newEmail())) {
      throw new IllegalArgumentException("Email already exits " + userUpdateDTO.newEmail());
    }
    //업데이트
    user.update(userUpdateDTO.newUsername(), userUpdateDTO.newEmail(), userUpdateDTO.newPassword());
    userRepository.save(user);

    //프로필 이미지 저장 (선택적으로)
    if (binaryContentCreateRequest != null &&
        binaryContentCreateRequest.bytes() != null
        && binaryContentCreateRequest.bytes().length > 0) {
      //기존 이미지 삭제 , 이미지 저장
      binaryContentRepository.deleteByUserId(user.getId());
      BinaryContent binaryContent = new BinaryContent(
          UUID.randomUUID(),
          user.getId(),
          null,
          binaryContentCreateRequest.fileName(),
          binaryContentCreateRequest.contentType(),
          binaryContentCreateRequest.bytes()
      );
      binaryContentRepository.save(binaryContent);
    }
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElse(null);
    return UserResponseDTO.fromEntity(user, userStatus,
        binaryContentRepository.findByUserId(user.getId()));
  }

  @Override
  public void delete(UUID userId) {
    //사용자 찾고
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    //관련 데이터 삭제(첨부파일, 유저 상태)
    if (binaryContentRepository.existsByUserId(userId)) {
      binaryContentRepository.deleteByUserId(user.getId());
    }
    if (userStatusRepository.existByUserId(userId)) {
      userStatusRepository.deleteByUserId(user.getId());
    }

    //user 삭제
    userRepository.deleteById(userId);
  }


}
