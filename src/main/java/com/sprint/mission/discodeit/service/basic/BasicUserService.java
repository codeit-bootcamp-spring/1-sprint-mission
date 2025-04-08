package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.status.UserStateService;
import com.sprint.mission.discodeit.service.util.BinaryContentUtils;
import com.sprint.mission.discodeit.service.util.UserStatusContextUtils;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStateService userStateService;
  private final BinaryContentStorage binaryContentStorage;

  @PersistenceContext
  EntityManager em;

  /**
   * 유저 생성
   */
  @Override
  public UserCreateResponse join(UserCreateRequest request, MultipartFile file)
      throws IOException {
    User findUser = userRepository.findUserByUsername(request.getUsername());
    User findEmail = userRepository.findUserByEmail(request.getEmail());
    if (findUser != null) {
      log.error("중복 이름 '{}' 저장 시도", request.getUsername());
      throw new DuplicateUsernameException(Instant.now(), ErrorCode.EXIST_USER,
          Map.of(request.getUsername(), ErrorCode.EXIST_USER.getMessage()));
    } else if (findEmail != null) {
      log.error("중복 이메일 '{}' 저장 시도", request.getEmail());
      throw new DuplicateEmailException(Instant.now(), ErrorCode.EXIST_USER,
          Map.of(request.getUsername(), ErrorCode.EXIST_USER.getMessage()));
    }

    BinaryContent profile = BinaryContentUtils.getProfile(file);
    User savedMember = saveUser(request, profile);

    UserStatusContextUtils.saveUserStatus(savedMember);
    BinaryContentUtils.saveProfileImg(file, savedMember);

    return new UserCreateResponse(
        savedMember.getId(),
        savedMember.getUsername(),
        savedMember.getEmail(),
        savedMember.getProfile() != null ? BinaryContentMapper.toDto(savedMember.getProfile())
            : null,
        true);
  }

  /**
   * userId로 유저 찾기(단건)
   */
  @Transactional(readOnly = true)
  @Override
  public UserCreateResponse findById(UUID userId) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
            Map.of(userId.toString(), ErrorCode.USER_NOT_FOUND.getMessage())));

    return new UserCreateResponse(findUser.getId(), findUser.getUsername(), findUser.getEmail(),
        BinaryContentMapper.toDto(findUser.getProfile()), true);
  }

  /**
   * 모든 유저 찾기
   */
  @Override
  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<UserCreateResponse> findAll() {
    List<User> users = userRepository.findUsers();

    return users.stream()
        .map(user -> new UserCreateResponse(user.getId(), user.getUsername(), user.getEmail(),
            BinaryContentMapper.toDto(user.getProfile()), user.isThereHere()))
        .toList();
  }

  /**
   * 유저 삭제
   */
  @Override
  public UUID delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
          Map.of(userId.toString(), ErrorCode.USER_NOT_FOUND.getMessage())
      );
    }
    userRepository.deleteById(userId);

    log.info("유저 삭제: {}", userId);
    return userId;
  }

  /**
   * 유저 업데이트
   */
  @Override
  public UserUpdateResponse update(UUID userId, UserUpdateRequest request,
      MultipartFile profile) throws IOException {

    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("유저 찾기 실패: {}", userId);
          return new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
              Map.of(userId.toString(), ErrorCode.USER_NOT_FOUND.getMessage())
          );
        });

    changeUser(findUser, request, profile);

    log.info("유저 수정: {}", userId);
    return new UserUpdateResponse(userId, findUser.getUsername(), findUser.getEmail(),
        BinaryContentMapper.toDto(findUser.getProfile()), true);
  }

  /**
   * 유저 상태 업데이트
   */
  @Override
  public UserStatusUpdateResponse updateOnlineStatus(UUID userId, UserStatusUpdateRequest request) {

    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
            Map.of(userId.toString(), ErrorCode.USER_NOT_FOUND.getMessage())));

    findUser.getStatus().setLastActiveAt(request.newLastActiveAt());

    return new UserStatusUpdateResponse(userId, findUser.getId(), request.newLastActiveAt());
  }

  /**
   * 유저 정보 변경
   */
  private void changeUser(User findUser, UserUpdateRequest request, MultipartFile file)
      throws IOException {
    String newName = request.newUsername();
    String newEmail = request.newEmail();
    String newPassword = request.newPassword();
    if (newName == null) {
      newName = findUser.getUsername();
    }
    if (newEmail == null) {
      newEmail = findUser.getEmail();
    }
    if (newPassword == null) {
      newPassword = findUser.getPassword();
    }

    findUser.changeUserInfo(newName, newEmail,
        newPassword);

    BinaryContent profile = BinaryContentUtils.getProfile(file);
    if (profile != null) {
      findUser.changeProfile(profile);
    }

    em.flush();
    em.clear();

    BinaryContentUtils.saveProfileImg(file, findUser);
  }

  /**
   * 유저 저장 메서드
   */
  private User saveUser(UserCreateRequest request, BinaryContent bin) {
    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(request.getPassword())
        .profile(bin)
        .build();

    log.info("유저 저장: {}", user.getUsername());
    return userRepository.save(user);
  }

}