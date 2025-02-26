package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.binarycontent.UploadBinaryContent;
import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.entity.status.user.UserStatusRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserCommonRequest;
import com.sprint.mission.discodeit.entity.user.UserCommonResponse;
import com.sprint.mission.discodeit.entity.user.UserResponse;
import com.sprint.mission.discodeit.entity.user.UserRole;
import com.sprint.mission.discodeit.entity.user.create.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.create.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.create.UserCreateWithBinaryContentResponse;
import com.sprint.mission.discodeit.entity.user.find.UserFindResponse;
import com.sprint.mission.discodeit.entity.user.update.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.update.UserUpdateResponse;
import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import com.sprint.mission.discodeit.exception.user.UserExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.status.UserStateService;
import com.sprint.mission.discodeit.service.validate.UserServiceValidator;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStateService userStateService;
  private final BinaryContentService binaryContentService;

  private static final UserServiceValidator validator = new UserServiceValidator();


  @PostConstruct
  public void init() {
    log.error("주입된 userRepository: {}", userRepository.getClass().getSimpleName());
  }

  /**
   * 스프린트 미션 5 요구사항 user 등록
   */
  @Override
  public UserCreateResponse createUserWithProfile(UserCreateRequest request,
      MultipartFile file) throws IOException {

    // 중복 체크
    checkDuplicated(request.username(), request.email());

    if (file == null) {
      User user = buildUser(request, null);
      saveUserStatus(user.getId(), user.getUserName());

      userRepository.userSave(user);
      return getUserCreateResponse(user);
    }

    String savedFileName = getSavedFile(file);

    User user = buildUser(request, savedFileName);

    saveUserStatus(user.getId(), user.getUserName());
    userRepository.userSave(user);

    return getUserCreateResponse(user);
  }

  /**
   * 스프린트 미션 5 요구사항, user 목록 조회
   */
  @Override
  public List<UserFindResponse> findAllUsers() {
    Map<UUID, User> allUser = userRepository.findAllUser();

    return allUser.values().stream()
        .map(user -> new UserFindResponse(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUserName(),
            user.getUserEmail(),
            user.getProfileId(),
            userStateService.find(user.getId()).getState().equals("online")
        ))
        .toList();
  }

  /**
   * 스프린트 미션 5, 유저 업데이트, 프사도 같이 가능
   */
  @Override
  public UserUpdateResponse updateUser(UUID userId, UserUpdateRequest request, MultipartFile file)
      throws IOException {

    checkNull(userId);
    checkDuplicated(request.newUsername(), request.newEmail());

    if (file == null) {
      return savedNoneProfileImgUser(userId, request);
    }

    String savedFileName = getSavedFile(file);

    User updateUser = updateUserInfo(userId, request, savedFileName);

    return getUserUpdateResponse(updateUser);
  }

  @Override
  public UserCommonResponse createUser(UserCommonRequest user) {
    if (user == null) {
      throw new UserNotFoundException("null user");
    }
    checkDuplicated(user.userName(), user.userEmail());

    UUID userId = UUID.randomUUID();
    return createBasicUser(userId, user);
  }

  @Override
  public UserCreateWithBinaryContentResponse createUserWithProfile(UserCommonRequest createDto,
      BinaryContentRequest request) {
    UserCommonResponse userResponse = createUser(createDto);
    request.updateId(userResponse.id()); // channelId는 null인 상태, requestUserId는 있음

    UserCreateWithBinaryContentResponse userWithFileResponse = new UserCreateWithBinaryContentResponse(
        userResponse.id(),
        userResponse.userName(),
        userResponse.userEmail(),
        request.getFileName()
    );

    if (request.getFile() == null && request.getFiles().isEmpty()) {
      log.info("유저 생성 완료: {}", userWithFileResponse.getUserName());
      return userWithFileResponse;
    }

    try {
      User user = userRepository.findUserByEmail(createDto.userEmail());
      if (user == null) {
        throw new UserNotFoundException("null user");
      }

      // 프로필 이미지 업로드 및 저장
      List<UploadBinaryContent> uploadBinaryContents = binaryContentService.create(request);
      log.info("프사 있는 유저 생성: {}", user.getUserName());

      UploadBinaryContent uploadBinaryContent = uploadBinaryContents.get(0);
      userWithFileResponse.setFileName(uploadBinaryContent.getSavedFileName());

      return userWithFileResponse;
    } catch (IOException e) {
      log.error("유저 생성 실패: {}", createDto.userName(), e);
      throw new IllegalUserException("프로필 이미지 업로드 중 오류가 발생했습니다: " + e.getMessage());
    }
  }

  @Override
  public UserResponse find(UUID userId) {
    User user = userRepository.findUserById(userId);
    if (user == null) {
      throw new UserNotFoundException("null user");
    }

    UserStatus status = userStateService.find(userId);
    BinaryContentResponse profileImage = null;
    try {
      profileImage = binaryContentService.find(userId.toString());
    } catch (Exception e) {
      log.debug("No profile image found for user: {}", userId);
    }

    return new UserResponse(user.getUserName(), user.getUserEmail(), status.getState());
  }

  // 유저 이름으로 찾기

  @Override
  public UserCommonResponse find(String userName) {
    User findUser = userRepository.findUserByName(userName);

    if (findUser == null) {
      throw new UserNotFoundException(userName);
    }

    return convertToUserResponse(findUser.getId(), findUser.getUserName(), findUser.getUserEmail());
  }

  /**
   * 사용자의 온라인 상태도 같이 포함 패스워드 정보 X
   */
  @Override
  public List<UserResponse> findAll() {
    Map<UUID, User> allUsersMap = Optional.ofNullable(userRepository.findAllUser())
        .orElseThrow(() -> new UserNotFoundException("null"));

    return allUsersMap.entrySet().stream()
        .map(entry -> new UserResponse(
            entry.getValue().getUserName(),
            entry.getValue().getUserEmail(),
            userStateService.find(entry.getKey()).getState(),
            binaryContentService.findBinaryContentByUserId(entry.getKey())
                .map(bin -> bin.getUploadFile().getSavedFileName())
                .orElse("default_profile.png")
        )).toList();

    //return convertToUserResponseMap(allUsersMap);
  }

  /**
   * - [ ] 선택적으로 프로필 이미지를 대체할 수 있습니다. - [ ] DTO를 활용해 파라미터를 그룹화합니다. - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
   */
  @Override
  public UserCommonResponse update(String userName, UserCommonRequest updateDto) {
    User findUser = userRepository.findUserByName(userName);
    if (findUser == null) {
      throw new UserNotFoundException();
    }

    findUser.updateName(updateDto.userName());
    findUser.updateEmail(updateDto.userEmail());
    findUser.updatePassword(updateDto.userPassword());

    userStateService.updateByUserName(userName, updateDto);
    userRepository.userSave(findUser);

    return new UserCommonResponse(findUser.getId(), findUser.getUserName(),
        findUser.getUserEmail());
  }


  /**
   * 프사도 바꿀 수 있음
   */
  @Override
  public UserCommonResponse updateUserWithProfile(UUID userId, BinaryContentRequest binaryContent) {
    if (userId == null) {
      throw new IllegalUserException("User ID cannot be null");
    }

    User user = userRepository.findUserById(userId);
    if (user == null) {
      throw new UserNotFoundException();
    }

    updateUserStat(userId);

    List<BinaryContentResponse> existingProfiles = binaryContentService.findAllById(userId);

    // 기존 프로필 이미지 삭제
    for (BinaryContentResponse profile : existingProfiles) {
      binaryContentService.delete(profile.id());
    }

    // 새 프로필 이미지 업로드
    if (binaryContent != null && (binaryContent.getFile() != null || !binaryContent.getFiles()
        .isEmpty())) {
      try {
        List<UploadBinaryContent> newProfiles = binaryContentService.create(binaryContent);
        log.info("Updated profile images for user: {}, number of new files: {}",
            user.getUserName(), newProfiles.size());
      } catch (IOException e) {
        log.error("Failed to update profile images for user: {}", userId, e);
        throw new IllegalUserException("프로필 이미지 업데이트 중 오류가 발생했습니다: " + e.getMessage());
      }
    }

    return convertToUserResponse(user.getId(), user.getUserName(), user.getUserEmail());
  }


  /**
   * 관련된 도메인도 같이 삭제합니다. - `BinaryContent`(프로필), `UserStatus`
   */
  @Override
  public UUID deleteUser(UUID userId) {
    if (userId == null) {
      throw new IllegalUserException("userId를 다시 확인해주세요.");
    }

    // 프로필 이미지 삭제
    List<BinaryContentResponse> profiles = binaryContentService.findAllById(userId);
    for (BinaryContentResponse profile : profiles) {
      binaryContentService.delete(profile.id());
    }

    userRepository.removeUserById(userId);
    userStateService.delete(userId);

    log.info("User deleted successfully: {}", userId);
    return userId;
  }

  private void updateUserStat(UUID userId) {
    Map<UUID, UserStatus> findUserStatMap = userStateService.findAllByUserId(userId);
    UserStatus userStatus = findUserStatMap.values().stream()
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("찾는 userStatus가 없습니다."));
    userStatus.updateUserStatus();
    userStateService.update(new UserStatusRequest(userStatus.getUserId(), userStatus.getUserName(),
        userStatus.getState()));
  }

  private UserCommonResponse createBasicUser(UUID userId, UserCommonRequest user) {
    if (validator.isNullParam(user.userName(), user.userPassword())) {
      throw new IllegalUserException();
    }

    saveUser(userId, user);
    saveUserStatus(userId, user.userName());

    log.warn("유저 등록: {}", user.userName());

    return new UserCommonResponse(userId, user.userName(), user.userEmail());
  }

  private UserUpdateResponse savedNoneProfileImgUser(UUID userId, UserUpdateRequest request) {
    User user = userRepository.findUserById(userId);

    checkNullAndUpdateUser(request, user);

    userRepository.userSave(user);
    return getUserUpdateResponse(user);
  }

  private void saveUserStatus(UUID userId, String username) {
    UserStatus userStatus = UserStatus.createUserStatus(userId, username);
    userStateService.create(new UserStatusRequest(userStatus.getUserId(), userStatus.getUserName(),
        userStatus.getState()));
  }

  private static UserUpdateResponse getUserUpdateResponse(User updateUser) {
    return new UserUpdateResponse(updateUser.getId(), updateUser.getCreatedAt(), Instant.now(),
        updateUser.getUserName(), updateUser.getUserEmail(), updateUser.getUserPassword(),
        updateUser.getProfileId());
  }

  private void saveUser(UUID userId, UserCommonRequest user) {
    User createUser = User.createUser(userId, user, UserRole.ROLE_COMMON);
    userRepository.userSave(createUser);
  }

  private String getSavedFile(MultipartFile file) throws IOException {
    BinaryContentRequest binaryContent = BinaryContentRequest.builder()
        .fileName(file.getOriginalFilename())
        .file(file)
        .build();

    List<UploadBinaryContent> uploadBinaryContents = binaryContentService.create(binaryContent);
    return Objects.requireNonNull(uploadBinaryContents.stream()
            .findFirst()
            .orElse(null))
        .getSavedFileName();
  }

  private void checkDuplicated(String name, String email) {
    /**
     * `username`과 `email`은 다른 유저와 같으면 안됩니다.
     */
    Map<UUID, User> allUsers = userRepository.findAllUser();
    if (allUsers.isEmpty()) {
      return;
    }

    boolean isSameUser = allUsers.values().stream()
        .anyMatch(users -> users.getUserName().equals(name));

    boolean isSameEmail = allUsers.values().stream()
        .anyMatch(users -> users.getUserEmail().equals(email));

    if (isSameEmail) {
      throw new UserExistsException(email);
    } else if (isSameUser) {
      throw new UserExistsException(name);
    }
  }

  private static UserCommonResponse convertToUserResponse(UUID id, String userName,
      String userEmail) {
    return new UserCommonResponse(id, userName, userEmail);
  }

  private Map<UUID, UserResponse> convertToUserResponseMap(Map<UUID, User> allUsersMap) {
    return allUsersMap.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> {
              UserStatus status = userStateService.find(entry.getKey());
              Optional<BinaryContent> binaryContentByUserId = binaryContentService.findBinaryContentByUserId(
                  entry.getValue().getId());
              return new UserResponse(
                  entry.getValue().getUserName(),
                  entry.getValue().getUserEmail(),
                  status.getState(),
                  binaryContentByUserId
                      .map(content -> content.getUploadFile().getSavedFileName())
                      .orElse(null)
              );
            }
        ));
  }

  private static UserCreateResponse getUserCreateResponse(User user) {
    return new UserCreateResponse(user.getId(), Instant.now(), Instant.now(), user.getUserName(),
        user.getUserEmail(), user.getUserPassword(),
        user.getProfileId());
  }

  private static User buildUser(UserCreateRequest request, String savedFileName) {
    return User.builder()
        .userName(request.username())
        .userEmail(request.email())
        .userPassword(request.password())
        .profileId(savedFileName)
        .build();
  }

  private User updateUserInfo(UUID userId, UserUpdateRequest request, String savedFile) {
    User findUser = userRepository.findUserById(userId);

    checkNullAndUpdateUser(request, findUser);
    if (savedFile != null) {
      findUser.setProfileId(savedFile);
    }

    return userRepository.userSave(findUser);
  }

  private void checkNull(UUID userId) {
    User user = userRepository.findUserById(userId);
    if (user == null) {
      throw new UserNotFoundException(userId.toString());
    }
  }

  private static void checkNullAndUpdateUser(UserUpdateRequest request, User findUser) {
    if (request.newUsername() != null && !request.newUsername().isEmpty()) {
      findUser.setUserName(request.newUsername());
    }

    if (request.newEmail() != null && !request.newEmail().isEmpty()) {
      findUser.setUserEmail(request.newEmail());
    }

    if (request.newPassword() != null && !request.newPassword().isEmpty()) {
      findUser.setUserPassword(request.newPassword());
    }
  }
}
