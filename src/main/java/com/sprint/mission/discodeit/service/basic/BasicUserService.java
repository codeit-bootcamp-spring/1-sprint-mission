package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.async.event.RoleChangedNotificationEvent;
import com.sprint.mission.discodeit.common.NotificationType;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.data.DataUpdateFailedException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserUpdateDataNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.security.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.SseService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.validation.Validator;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  @Qualifier("userValidator")
  private final Validator<User, UserSignupRequestDto, UserUpdateDto> userValidator;

  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

//  private final SessionRegistry sessionRegistry;

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentService binaryContentService;
  private final JwtSessionRepository jwtSessionRepository;
  private final JwtService jwtService;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final SseService sseService;

  // TODO: LoadData Entity Name Magic Number를 어떻게 하면 좋을까?

  @Transactional
  @CacheEvict(value = "allUsers")
  @Override
  public UserResponseDto create(UserSignupRequestDto userReqDto, MultipartFile profile)
      throws IOException {
    // 유저 생성 로직
    log.debug("사용자 생성 요청 - 요청 데이터: {}", userReqDto);
    // 유효성 검사
    userValidator.validateCreate(userReqDto);

    String hashedPassword = passwordEncoder.encode(userReqDto.password());

    // 중복 검사
    if (userRepository.existsUserByEmail(userReqDto.email()) || userRepository.existsUserByUsername(
        userReqDto.userName())) {
      log.warn("사용자가 이미 존재합니다. - email: {}, userName: {}", userReqDto.email(),
          userReqDto.userName());
      throw new UserAlreadyExistsException(userReqDto.email(), userReqDto.userName());
    }

    // user 생성
    User user = userMapper.toEntity(userReqDto, hashedPassword);

    // 프로필 이미지 존재 시 생성
    if (profile != null) {
      BinaryContentResponseDto profileDto = binaryContentService.create(profile, user.getId());
      BinaryContent loadProfile = binaryContentRepository.findById(profileDto.id())
          .orElseThrow(() -> new BinaryContentNotFoundException(profileDto.id()));
      user.updateProfile(loadProfile);
      log.info("프로필 이미지가 등록되었습니다. - id: {}", loadProfile.getId());
    }
    UUID savedUser = userRepository.save(user).getId();
    User loadUser = userRepository.findById(savedUser)
        .orElseThrow(() -> new UserNotFoundException(savedUser));

    // 사용자 리프레시 알림
    sseService.sendUserRefresh(savedUser);

    log.info("사용자가 생성되었습니다. - id: {}", loadUser.getId());
    return userMapper.toResponseDto(loadUser, isUserOnline(loadUser.getUsername()));
  }

  @Override
  public UserResponseDto find(UUID userId) {
    log.debug("사용자 조회 요청 - id: {}", userId);
    User user = userRepository.findByIdWithDetails(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    if (user == null) {
      log.warn("해당 사용자가 존재하지 않습니다. - id: {}", user.getId());
      throw new UserNotFoundException(userId);
    }

    log.info("사용자 조회 성공 - id: {}", user.getId());
    return userMapper.toResponseDto(user, isUserOnline(user.getUsername()));
  }

  @Override
  @Cacheable(value = "allUsers")
  public List<UserResponseDto> findAll() {
    log.debug("전체 사용자 조회 요청");
    // sessionRegistry를 사용하지 않게 되어 리팩토링
    Set<String> onlineUsernames = jwtSessionRepository.findAll().stream()
        .filter(session -> session.getExpiresAt().isAfter(Instant.now()))
        .map(JwtSession::getUsername)
        .collect(Collectors.toSet());

    List<UserResponseDto> userList = userRepository.findAllWithDetails().stream()
        .map(user -> userMapper.toResponseDto(user, onlineUsernames.contains(user.getUsername())))
        .collect(Collectors.toList());
    log.info("전체 사용자 조회 성공 - 전체 사용자 수: {}", userList.size());
    return userList;
  }

  @Transactional
  @CacheEvict(value = "allUsers")
  @Override
  public UserResponseDto update(UUID userId, UserUpdateDto userUpdateDto,
      MultipartFile updateProfile) {
    log.debug("사용자 수정 요청 - 수정 대상 id: {}, 수정 요청 데이터: {}", userId, userUpdateDto);

    try {
      User current = userRepository.findById(userId)
          .orElseThrow(() -> new UserNotFoundException(userId));
      User updatedUser = userValidator.validateUpdate(current, userUpdateDto);
      if (updatedUser == null) {
        throw new UserUpdateDataNotFoundException(userId);
      }

      if (updateProfile != null) {
        // 프로필 데이터 존재 여부
        if (updatedUser.getProfile() != null) {
          binaryContentRepository.delete(updatedUser.getProfile());
        }

        BinaryContentResponseDto updateFile = binaryContentService.create(updateProfile, userId);
        BinaryContent update = binaryContentRepository.findById(updateFile.id())
            .orElseThrow(() -> new BinaryContentNotFoundException(updateFile.id()));
        updatedUser.updateProfile(update);
        log.info("사용자 프로필 이미지가 업데이트되었습니다. - id: {}, profileId: {}", updatedUser.getId(),
            updateFile.id());
      }

      userRepository.save(updatedUser); // DB에 반영

      // 사용자 리프레시 알림
      sseService.sendUserRefresh(updatedUser.getId());

      log.info("사용자 정보가 수정되었습니다. - id: {}", updatedUser.getId());

      return userMapper.toResponseDto(updatedUser, isUserOnline(updatedUser.getUsername()));
    } catch (UserUpdateDataNotFoundException ue) {
      log.warn("수정할 사용자 데이터가 없습니다. - 수정 대상 id: {}, 수정 요청 데이터: {}", userId, userUpdateDto);
      throw ue;
    } catch (Exception e) {
      log.error("사용자 수정 중 예외 발생 - id: {}, message: {}", userId, e.getMessage(), e);
      throw new DataUpdateFailedException("User", userId, e);
    }
  }

  @Transactional
  @CacheEvict(value = "allUsers")
  @Override
  public UserResponseDto delete(UUID userId) {
    log.debug("사용자 삭제 요청 - 삭제 대상 id: {}", userId);
    User deleteUser = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    userRepository.delete(deleteUser);

    // 사용자 리프레시 알림
    sseService.sendUserRefresh(deleteUser.getId());

    log.info("사용자가 삭제되었습니다. - id: {}", deleteUser.getId());
    return userMapper.toResponseDto(deleteUser, isUserOnline(deleteUser.getUsername()));
  }

  @Override
  public UserResponseDto getUserFromAuth(Authentication authentication) {
    log.info("세션을 통한 사용자 정보 조회 요청");
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AuthenticationCredentialsNotFoundException("is not authenticated");
    }
    log.info("isAuthenticated: {}", authentication.isAuthenticated());
    log.info("principal: {}", authentication.getPrincipal());
    log.info("principal class: {}",
        authentication.getPrincipal() != null ? authentication.getPrincipal().getClass() : "null");
    log.info("authorities: {}", authentication.getAuthorities());

    Object principal = authentication.getPrincipal();

    if (principal instanceof String principalStr && principalStr.equals("anonymousUser")) {
      throw new AuthenticationCredentialsNotFoundException("is anonymousUser");
    }

    if (!(principal instanceof CustomUserDetails userDetails)) {
      throw new AuthenticationCredentialsNotFoundException("is not CustomUserDetails");
    }

    User user = userDetails.getUser();
    log.info("인증된 사용자: userName={}, userId={}", user.getUsername(), user.getId());

    return userMapper.toResponseDto(user, isUserOnline(user.getUsername()));
  }

  @Override
  public String getUserFromRefreshToken(String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new AuthenticationCredentialsNotFoundException("Missing Refresh Token");
    }

    try {
      String username = jwtService.getUsernameFromToken(refreshToken);
      JwtSession session = jwtSessionRepository.findByUsername(username)
          .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Session not found"));

      if (!refreshToken.equals(session.getRefreshToken())) {
        throw new AuthenticationCredentialsNotFoundException("Invalid Refresh Token");
      }

      return session.getAccessToken();
    } catch (JwtException je) {
      throw new AuthenticationCredentialsNotFoundException(
          "Invalid Refresh Token: " + je.getMessage());
    }
  }

  @Override
  @CacheEvict(value = "allUsers")
  @Transactional
  public UserResponseDto updateUserRole(RoleUpdateRequest roleUpdateRequest,
      HttpServletRequest httpServletRequest) {
    log.info("사용자 역할 업데이트 요청");

    User user = userRepository.findById(roleUpdateRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(roleUpdateRequest.userId()));

    if (!user.getRole().equals(roleUpdateRequest.newRole())) {
      user.updateRole(roleUpdateRequest.newRole());
      userRepository.save(user);

      applicationEventPublisher.publishEvent(
          new RoleChangedNotificationEvent(roleUpdateRequest.userId(), roleUpdateRequest.userId(),
              NotificationType.ROLE_CHANGED));

      // 로그인 중이라면 JWTSession 제거 -> 강제 로그아웃
      Optional<JwtSession> sessionOptional = jwtSessionRepository.findByUsername(
          user.getUsername());
      sessionOptional.ifPresent(session -> {
        jwtSessionRepository.delete(session);
        log.info("사용자 역할 변경으로 인한 JWT 세션 무효화: {}", user.getUsername());
      });
    }

    return userMapper.toResponseDto(user, isUserOnline(user.getUsername()));
  }

  @Override
  public boolean isUserOnline(String username) {
    return jwtSessionRepository.existsByUsername(username);
  }
}
