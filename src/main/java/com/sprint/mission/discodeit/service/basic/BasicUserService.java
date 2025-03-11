package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.UserService;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Autowired
  private EntityManager entityManager;

  @Override
  @Transactional
  public UserDto createUser(UserCreateRequestDto request, MultipartFile profile) {
    if (request.getUsername() == null || request.getEmail() == null
        || request.getPassword() == null) {
      throw new IllegalArgumentException("사용자 정보를 올바르게 입력해야 합니다.");
    }
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("Email already in use");
    }
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("Name already in use");
    }

    BinaryContent profileImage =
        (profile != null && !profile.isEmpty()) ? saveProfile(profile) : null;

    User user = new User(request.getUsername(), request.getEmail(), request.getPassword(),
        profileImage);
    userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);
    user.setStatus(userStatus);

    return userMapper.toDto(user);
  }


  @Override
  public UserDto getUserById(UUID id) {
    return userRepository.findById(id)
        .map(userMapper::toDto)
        .orElseThrow(() -> new IllegalArgumentException("User id : " + id + " not found"));
  }

  @Override
  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream().map(userMapper::toDto).toList();
  }


  @Override
  @Transactional
  public UserDto updateUser(UUID userId, UserUpdateRequestDto request, MultipartFile profile) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found"));

    BinaryContent oldProfile = user.getProfile();

    if (profile != null && !profile.isEmpty()) {
      if (oldProfile == null || !oldProfile.getFileName().equals(profile.getOriginalFilename())) {
        if (oldProfile != null) {
          binaryContentStorage.delete(oldProfile.getId(),
              getFileExtension(oldProfile.getFileName()));
          binaryContentRepository.deleteById(oldProfile.getId());
        }

        BinaryContent newProfile = saveProfile(profile);
        user.setProfile(newProfile);
      }
    }

    user.update(request.getNewUsername(), request.getNewEmail(), request.getNewPassword());
    user.getStatus().update(Instant.now());

    return userMapper.toDto(user);
  }


  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
  }

  @Override
  @Transactional
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found"));

    // ✅ 먼저 프로필 이미지 삭제
    if (user.getProfile() != null) {
      UUID profileId = user.getProfile().getId();
      String extension = getFileExtension(user.getProfile().getFileName());
      binaryContentStorage.delete(profileId, extension);
      binaryContentRepository.delete(user.getProfile());
    }

    // ✅ 사용자 삭제
    userRepository.delete(user);
  }


  private BinaryContent saveProfile(MultipartFile profileFile) {
    if (profileFile == null || profileFile.isEmpty()) {
      throw new NoSuchElementException("Profile file is null or empty");
    }

    BinaryContent binaryContent = new BinaryContent(
        profileFile.getOriginalFilename(),
        profileFile.getSize(),
        profileFile.getContentType()
    );

    BinaryContent savedContent = binaryContentRepository.save(binaryContent);
    String extension = getFileExtension(profileFile.getOriginalFilename());

    try {
      Path filePath = binaryContentStorage.put(savedContent.getId(), profileFile.getBytes(),
          extension);
      savedContent.setFilePath(filePath.toString());
    } catch (IOException e) {
      throw new RuntimeException("Failed to save profile file", e);
    }

    return savedContent;
  }

  @Override
  @Transactional
  public BinaryContentDto saveProfileImage(MultipartFile profileFile) {
    if (profileFile == null || profileFile.isEmpty()) {
      throw new NoSuchElementException("multipartFile is null or empty");
    }
    BinaryContent binaryContent = new BinaryContent(
        profileFile.getOriginalFilename(),
        profileFile.getSize(),
        profileFile.getContentType()
    );
    BinaryContent savedContent = binaryContentRepository.save(binaryContent);

    String extension = getFileExtension(profileFile.getOriginalFilename());

    // 2️⃣ 생성된 ID를 사용하여 파일 저장
    try {
      Path filePath = binaryContentStorage.put(savedContent.getId(), profileFile.getBytes(),
          extension);
      binaryContent.setFilePath(filePath.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContentMapper.toDto(savedContent);
  }
}
