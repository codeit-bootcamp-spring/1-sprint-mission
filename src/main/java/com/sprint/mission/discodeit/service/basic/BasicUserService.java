package com.sprint.mission.discodeit.service.basic;

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
import com.sprint.mission.discodeit.security.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.validation.Validator;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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

  private final SessionRegistry sessionRegistry;

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentService binaryContentService;

  // TODO: LoadData Entity Name Magic Number를 어떻게 하면 좋을까?

  @Transactional
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
        userReqDto.username())) {
      log.warn("사용자가 이미 존재합니다. - email: {}, username: {}", userReqDto.email(),
          userReqDto.username());
      throw new UserAlreadyExistsException(userReqDto.email(), userReqDto.username());
    }

    // user 생성
    User user = userMapper.toEntity(userReqDto, hashedPassword);

    // 프로필 이미지 존재 시 생성
    if (profile != null) {
      BinaryContentResponseDto profileDto = binaryContentService.create(profile);
      BinaryContent loadProfile = binaryContentRepository.findById(profileDto.id())
          .orElseThrow(() -> new BinaryContentNotFoundException(profileDto.id()));
      user.updateProfile(loadProfile);
      log.info("프로필 이미지가 등록되었습니다. - id: {}", loadProfile.getId());
    }
    UUID savedUser = userRepository.save(user).getId();
    User loadUser = userRepository.findById(savedUser)
        .orElseThrow(() -> new UserNotFoundException(savedUser));

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
  public List<UserResponseDto> findAll() {
    log.debug("전체 사용자 조회 요청");

    Set<String> onlineUsernames = sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof UserDetails)
        .map(principal -> ((UserDetails) principal).getUsername())
        .collect(Collectors.toSet());

    List<UserResponseDto> userList = userRepository.findAllWithDetails().stream()
        .map(user -> userMapper.toResponseDto(user, onlineUsernames.contains(user.getUsername())))
        .collect(Collectors.toList());
    log.info("전체 사용자 조회 성공 - 전체 사용자 수: {}", userList.size());
    return userList;
  }

  @Transactional
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

        BinaryContentResponseDto updateFile = binaryContentService.create(updateProfile);
        BinaryContent update = binaryContentRepository.findById(updateFile.id())
            .orElseThrow(() -> new BinaryContentNotFoundException(updateFile.id()));
        updatedUser.updateProfile(update);
        log.info("사용자 프로필 이미지가 업데이트되었습니다. - id: {}, profileId: {}", updatedUser.getId(),
            updateFile.id());
      }

      userRepository.save(updatedUser); // DB에 반영

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
  @Override
  public UserResponseDto delete(UUID userId) {
    log.debug("사용자 삭제 요청 - 삭제 대상 id: {}", userId);
    User deleteUser = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    userRepository.delete(deleteUser);

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
    log.info("인증된 사용자: username={}, userId={}", user.getUsername(), user.getId());

    return userMapper.toResponseDto(user, isUserOnline(user.getUsername()));
  }

  @Override
  public UserResponseDto updateUserRole(RoleUpdateRequest roleUpdateRequest,
      HttpServletRequest httpServletRequest) {
    log.info("사용자 역할 업데이트 요청");

    User user = userRepository.findById(roleUpdateRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(roleUpdateRequest.userId()));

    if (!user.getRole().equals(roleUpdateRequest.newRole())) {
      user.updateRole(roleUpdateRequest.newRole());
      userRepository.save(user);

      // 현재 로그인 중인 사용자일 경우 세션 무효화
      SecurityContext context = (SecurityContext) httpServletRequest.getSession()
          .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

      if (context != null && context.getAuthentication().getName()
          .equalsIgnoreCase(user.getUsername())) {
        log.info("현재 로그인 된 사용자의 권한이 변경되어 세션을 무효화합니다.");
        httpServletRequest.getSession().invalidate();
      }
    }

    return userMapper.toResponseDto(user, isUserOnline(user.getUsername()));
  }

  @Override
  public boolean isUserOnline(String username) {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof UserDetails)
        .map(principal -> ((UserDetails) principal).getUsername())
        .anyMatch(name -> name.equals(username));
  }
}
