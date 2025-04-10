package com.sprint.mission.discodeit.service.Impl;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto create(UserDto dto, byte[] profileImage) {
    try {
      // 입력 값 검증
      validateUserInput(dto);

      // 이메일 중복 확인
      checkEmailDuplication(dto.getEmail());

      User user = User.builder()
          .name(dto.getName())
          .email(dto.getEmail())
          .password(dto.getPassword())
          .online(dto.isOnline())
          .build();

      if (profileImage != null && profileImage.length > 0) {
        user.setProfileImage(profileImage);
      }

      User saved = userRepository.save(user);
      return userMapper.toDto(saved);
    } catch (RestApiException e) {
      throw e;
    } catch (Exception e) {
      log.error("[회원가입 실패] 예상치 못한 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "회원가입 처리 중 오류가 발생했습니다.");
    }
  }

  @Transactional
  @Override
  public UserDto createWithProfileImage(UserDto dto, MultipartFile profileImage)
      throws IOException {
    try {
      // 입력 값 검증
      validateUserInput(dto);

      // 이메일 중복 확인
      checkEmailDuplication(dto.getEmail());

      User user = User.builder()
          .name(dto.getName())
          .email(dto.getEmail())
          .password(dto.getPassword())
          .online(dto.isOnline())
          .build();

      if (profileImage != null && !profileImage.isEmpty()) {
        handleProfileImage(user, profileImage, dto);
      }

      User saved = userRepository.save(user);
      return userMapper.toDto(saved);
    } catch (RestApiException e) {
      throw e;
    } catch (IOException e) {
      log.error("[회원가입 실패] 프로필 이미지 처리 중 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "프로필 이미지 처리 중 오류가 발생했습니다.");
    } catch (Exception e) {
      log.error("[회원가입 실패] 예상치 못한 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "회원가입 처리 중 오류가 발생했습니다.");
    }
  }

  private void handleProfileImage(User user, MultipartFile profileImage, UserDto dto)
      throws IOException {
    // BinaryContent 생성 및 저장
    String fileName = "profile_" + UUID.randomUUID().toString();
    Long size = profileImage.getSize();
    String contentType = profileImage.getContentType();

    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(fileName)
        .size(size)
        .contentType(contentType)
        .build();

    binaryContentRepository.save(binaryContent);

    // User 엔티티에 프로필 이미지 설정
    user.setProfileImage(profileImage.getBytes());
    user.setProfile(binaryContent);

    // UserDto의 profileImage 및 profile 필드 설정
    dto.setProfileImage(binaryContent.getId().toString());
    dto.setProfile(BinaryContentDto.builder()
        .id(binaryContent.getId())
        .fileName(binaryContent.getFileName())
        .contentType(binaryContent.getContentType())
        .size(binaryContent.getSize())
        .build());
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest updateRequest, byte[] profileImage) {
    try {
      User user = findUserById(userId);

      Objects.requireNonNull(updateRequest, "UserUpdateRequest cannot be null");

      // 필드 업데이트
      if (updateRequest.getNewUsername() != null && !updateRequest.getNewUsername().isEmpty()) {
        user.setName(updateRequest.getNewUsername());
      }

      if (updateRequest.getNewEmail() != null && !updateRequest.getNewEmail().isEmpty()) {
        // 이메일 중복 확인
        if (!user.getEmail().equals(updateRequest.getNewEmail())) {
          checkEmailDuplication(updateRequest.getNewEmail());
          user.setEmail(updateRequest.getNewEmail());
        }
      }

      if (updateRequest.getNewPassword() != null && !updateRequest.getNewPassword().isEmpty()) {
        user.setPassword(updateRequest.getNewPassword());
      }

      if (profileImage != null && profileImage.length > 0) {
        updateProfileImage(user, profileImage);
      }

      userRepository.save(user);
      return userMapper.toDto(user);
    } catch (RestApiException e) {
      throw e;
    } catch (Exception e) {
      log.error("[회원정보 수정 실패] 예상치 못한 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "회원정보 수정 중 오류가 발생했습니다.");
    }
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest updateRequest, MultipartFile profileImage)
      throws IOException {
    try {
      User user = findUserById(userId);

      Objects.requireNonNull(updateRequest, "UserUpdateRequest cannot be null");

      // 필드 업데이트
      if (updateRequest.getNewUsername() != null && !updateRequest.getNewUsername().isEmpty()) {
        user.setName(updateRequest.getNewUsername());
      }

      if (updateRequest.getNewEmail() != null && !updateRequest.getNewEmail().isEmpty()) {
        // 이메일 중복 확인
        if (!user.getEmail().equals(updateRequest.getNewEmail())) {
          checkEmailDuplication(updateRequest.getNewEmail());
          user.setEmail(updateRequest.getNewEmail());
        }
      }

      if (updateRequest.getNewPassword() != null && !updateRequest.getNewPassword().isEmpty()) {
        user.setPassword(updateRequest.getNewPassword());
      }

      if (profileImage != null && !profileImage.isEmpty()) {
        UserDto tempDto = new UserDto();
        handleProfileImage(user, profileImage, tempDto);
      }

      userRepository.save(user);
      return userMapper.toDto(user);
    } catch (RestApiException e) {
      throw e;
    } catch (IOException e) {
      log.error("[회원정보 수정 실패] 프로필 이미지 처리 중 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "프로필 이미지 처리 중 오류가 발생했습니다.");
    } catch (Exception e) {
      log.error("[회원정보 수정 실패] 예상치 못한 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "회원정보 수정 중 오류가 발생했습니다.");
    }
  }

  private void updateProfileImage(User user, byte[] profileImage) {
    String fileName = "profile_" + UUID.randomUUID().toString();
    Long size = (long) profileImage.length;
    String contentType = "image/jpeg"; // 기본값

    BinaryContent newProfile = BinaryContent.builder()
        .fileName(fileName)
        .size(size)
        .contentType(contentType)
        .build();

    binaryContentRepository.save(newProfile);
    user.setProfileImage(profileImage);
    user.setProfile(newProfile);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    try {
      User user = findUserById(userId);

      userRepository.delete(user);

      if (user.getProfile() != null) {
        binaryContentRepository.deleteById(user.getProfile().getId());
      }
    } catch (RestApiException e) {
      throw e;
    } catch (Exception e) {
      log.error("[회원 삭제 실패] 예상치 못한 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "회원 삭제 중 오류가 발생했습니다.");
    }
  }

  @Override
  public UserDto find(UUID userId) {
    User user = findUserById(userId);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto findByEmail(String email) {
    if (email == null || email.isEmpty()) {
      throw new RestApiException(DomainErrorCode.INVALID_INPUT, "이메일은 필수 입력값입니다.");
    }

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> {
          log.error("[사용자 조회 실패] 존재하지 않는 이메일: '{}'", email);
          return new RestApiException(DomainErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다.");
        });

    return userMapper.toDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();
    if (users == null) {
      log.warn("경고: userRepository.findAll()이 null을 반환했습니다.");
      return new ArrayList<>();
    }

    return users.stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public void updateOnlineStatus(UUID userId, boolean online) {
    try {
      User user = findUserById(userId);
      user.setOnline(online);
      userRepository.save(user);
    } catch (RestApiException e) {
      throw e;
    } catch (Exception e) {
      log.error("[상태 업데이트 실패] 예상치 못한 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "상태 업데이트 중 오류가 발생했습니다.");
    }
  }

  @Transactional
  @Override
  public UserStatusDto updateUserStatus(UUID userId, UserStatusUpdateRequest updateRequest) {
    try {
      User user = findUserById(userId);
      Instant lastActiveAt = updateRequest.getNewLastActiveAt();

      return new UserStatusDto(userId, lastActiveAt);
    } catch (RestApiException e) {
      throw e;
    } catch (Exception e) {
      log.error("[사용자 상태 업데이트 실패] 예상치 못한 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.USER_SERVER_ERROR, "사용자 상태 업데이트 중 오류가 발생했습니다.");
    }
  }

  // 공통 검증 메서드들
  private User findUserById(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> {
          log.debug("[사용자 조회 실패] 존재하지 않는 ID: '{}'", id);
          return new RestApiException(DomainErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다.");
        });
  }

  private void validateUserInput(UserDto dto) {
    if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
      throw new RestApiException(DomainErrorCode.INVALID_INPUT, "이메일은 필수 입력값입니다.");
    }

    if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
      throw new RestApiException(DomainErrorCode.INVALID_INPUT, "비밀번호는 필수 입력값입니다.");
    }

    if (dto.getName() == null || dto.getName().isEmpty()) {
      throw new RestApiException(DomainErrorCode.INVALID_INPUT, "이름은 필수 입력값입니다.");
    }
  }

  private void checkEmailDuplication(String email) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new RestApiException(DomainErrorCode.DUPLICATED_EMAIL, "이미 사용중인 이메일입니다.");
    }
  }
}
