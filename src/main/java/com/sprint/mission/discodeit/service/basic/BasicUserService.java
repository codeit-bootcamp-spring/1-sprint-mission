package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.role.Role;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.jwt.JwtTokenNotFoundException;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.status.UserSessionService;
import com.sprint.mission.discodeit.service.util.BinaryContentUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final PasswordEncoder passwordEncoder;
  private final UserSessionService userSessionService;
  private final JwtSessionRepository jwtSessionRepository;
  private final JwtService jwtService;

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
    checkDuplicated(request, findUser, findEmail);

    BinaryContent profile = BinaryContentUtils.getProfile(file);
    User savedMember = saveUser(request, profile);

    BinaryContentUtils.saveProfileImg(file, savedMember, binaryContentStorage);

    return new UserCreateResponse(
        savedMember.getId(),
        savedMember.getUsername(),
        savedMember.getEmail(),
        savedMember.getProfile() != null ? BinaryContentMapper.toDto(savedMember.getProfile())
            : null,
        true);
  }

  private void checkDuplicated(UserCreateRequest request, User findUser, User findEmail) {
    if (findUser != null) {
      log.error("중복 이름 '{}' 저장 시도", request.getUsername());
      throw new DuplicateUsernameException(Instant.now(), ErrorCode.EXIST_USER,
          Map.of(request.getUsername(), ErrorCode.EXIST_USER.getMessage()));
    } else if (findEmail != null) {
      log.error("중복 이메일 '{}' 저장 시도", request.getEmail());
      throw new DuplicateEmailException(Instant.now(), ErrorCode.EXIST_USER,
          Map.of(request.getUsername(), ErrorCode.EXIST_USER.getMessage()));
    }
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
   * @methodName : findByUsername
   * @date : 2025. 5. 19. 13:44
   * @author : wongil
   * @Description: username으로 찾기
   **/
  @Override
  public UserDto findByUsername(String username) {
    User user = userRepository.findUserByUsername(username);

    return UserDto.builder()
        .username(user.getUsername())
        .id(user.getId())
        .email(user.getEmail())
        .build();
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
            BinaryContentMapper.toDto(user.getProfile()),
            userSessionService.isOnline(user.getUsername()))) // TODO: 세션으로 구현후 바꾸기
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

    readStatusRepository.deleteAllByuser_id(userId);
    messageRepository.deleteAllByauthor_id(userId);

    userRepository.deleteById(userId);

    log.info("유저 삭제: {}", userId);
    return userId;
  }

  /**
   * 유저 업데이트
   */
  @Override
  public UserUpdateResponse update(UUID userId, UserUpdateRequest request,
      MultipartFile profile, String refreshToken) throws IOException {

    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("유저 찾기 실패: {}", userId);
          return new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
              Map.of(userId.toString(), ErrorCode.USER_NOT_FOUND.getMessage())
          );
        });

    changeUser(findUser, request, profile);
    changeJwtAccessToken(userId, refreshToken, findUser);

    log.info("유저 수정: {}", userId);
    return new UserUpdateResponse(userId, findUser.getUsername(), findUser.getEmail(),
        BinaryContentMapper.toDto(findUser.getProfile()), true);
  }

  private void changeJwtAccessToken(UUID userId, String refreshToken, User findUser) {
    // TODO: 유저 정보 변경하면 기존의 토큰 정보가 바뀌지 않아서 프로필 사진이 제대로 안나옴(로그아웃하고 다시 접속하면 됨)
    JwtSession jwtSession = getJwtSession(refreshToken);
    UserDto userDto = getUserDto(userId, findUser);
    String accessToken = jwtService.createAccessToken(userDto);
    jwtSession.setAccessToken(accessToken);

    log.info("Jwt Access Token 변경 완료: {}", accessToken);
  }

  private JwtSession getJwtSession(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(
            () -> new JwtTokenNotFoundException(Instant.now(), ErrorCode.NOT_FOUND_JWT, Map.of(
                ErrorCode.NOT_FOUND_JWT.getCode(),
                ErrorCode.NOT_FOUND_JWT.getMessage()
            )));
  }

  private UserDto getUserDto(UUID userId, User findUser) {
    return UserDto.builder()
        .id(userId)
        .username(findUser.getUsername())
        .email(findUser.getEmail())
        .online(userSessionService.isOnline(findUser.getUsername())) // TODO: 토큰기반으로 변경후 수정
        .profile(
            new BinaryContentDto(findUser.getProfile().getId(), findUser.getProfile().getFileName(),
                findUser.getProfile().getSize(), findUser.getProfile().getContentType()))
        .Role(findUser.getRole())
        .build();
  }

  /**
   * 유저 상태 업데이트
   */
  @Override
  public UserStatusUpdateResponse updateOnlineStatus(UUID userId, UserStatusUpdateRequest request) {

    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
            Map.of(userId.toString(), ErrorCode.USER_NOT_FOUND.getMessage())));

//    findUser.getStatus().setLastActiveAt(request.newLastActiveAt());

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

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(encodedPassword)
        .profile(bin)
        .role(Role.ROLE_USER)
        .build();

    log.info("유저 저장: {}", user.getUsername());
    return userRepository.save(user);
  }

}