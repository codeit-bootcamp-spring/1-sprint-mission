package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
// final/nonnull 이라 값이 반드시 있어야 하는 생성자 자동 생성, @Autowired 안써줘도 생성자가 하나일 때는 자동으로 의존성 주입 (+생성자 있으면 자바단에서 기본생성자 생성 안함)
@Service
public class BasicUserService extends UserMapper implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest request,
      Optional<MultipartFile> nullableFile) {
    String email = request.getEmail();
    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExistException(null); // TODO : details 어떤식으로 전달해야될지 모르겠음
    }

    // 프로필 이미지 설정
    MultipartFile file = nullableFile.orElse(null);
    BinaryContent profile = BinaryContent.builder()
        .fileName(file.getOriginalFilename())
        .size((int) file.getSize())
        .contentType((file.getContentType()))
        .build();

    byte[] data = null;
    try {
      data = file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String username = request.getUsername();
    String password = request.getPassword();
    User newUser = User.builder()
        .username(username)
        .email(email)
        .password(password)
        .profile(profile)
        .build();

    binaryContentRepository.save(profile);
    binaryContentStorage.put(profile.getId(), data); // 레포지토리 save를 '먼저' 해야 id가 생성되고, id가 있으니 이 경로로 put이 가능해짐

    User user = userRepository.save(newUser);

    // 생성된 회원 user status 설정
    Instant lastActiveAt = Instant.now();
    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(lastActiveAt)
        .build();
    userStatusRepository.save(userStatus);

    UserDto createdUser = toDto(user);
    return createdUser;
  }

  @Transactional(readOnly = true)
  @Override
  // TODO : 회원을 찾고 반환하는 값으로 password를 주진 않을 거잖아 -> 보안 차원의 DTO
  public UserDto find(UUID userId) {
    // 반환타입이 UserDto니까, mapper을 이용해 Optional의 null이 아닌 경우인 user 변수를 dto로 변경해서 return
    return userRepository.findById(userId)
        .map(user -> toDto(user))
        .orElseThrow(() -> new UserNotFoundException(null));
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream() //TODO : List<User>를 Stream<User>로 변환 -> "User 객체들이 순차적으로 처리될 준비가 된 상태" -> 그 다음 action인 mapper로.
        .map(user -> toDto(user))
        .toList(); // 스트림 메소드. 스트림 상태인 객체들을 리스트로 변환
  }


  @Transactional
  @Override
  // TODO : 업뎃할거야. User 엔티티의 모든 필드를 업뎃요소로 줄 필요는 없어 -> DTO 만들자
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<MultipartFile> nullableFile) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(null));

    String newUsername = userUpdateRequest.getNewUsername();
    String newEmail = userUpdateRequest.getNewEmail();
    if (userRepository.existsByEmail(newEmail)) {
      throw new UserAlreadyExistException(null);
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new UserAlreadyExistException(null);
    }

    MultipartFile file = nullableFile.orElse(null);
    if (file != null) {
      binaryContentRepository.deleteById(user.getProfile().getId());
    } // DB에서 기존 프로필 삭제해주는 로직

    String fileName = file.getOriginalFilename();
    String contentType = file.getContentType();
    int size = (int) file.getSize();
    byte[] data = null;
    try {
      data = file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    BinaryContent newProfile = BinaryContent.builder()
        .fileName(fileName)
        .size(size)
        .contentType(contentType)
        .build();

    binaryContentStorage.put(newProfile.getId(), data);

    binaryContentRepository.save(newProfile);

    String newPassword = userUpdateRequest.getNewPassword();
    user.update(newUsername, newEmail, newPassword, newProfile);

    return toDto(user);
  }


  @Transactional
  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(null));

    Optional.ofNullable(user.getProfile()).map(BinaryContent::getId)
        .ifPresent(
            binaryContentRepository::deleteById); // BinaryContent::getId는 BinaryContent 객체가 존재하면 그 객체의 getId() 메소드를 호출하는 것
    userStatusRepository.deleteByUserId(userId); // 관련 도메인 삭제 (프로필, 회원상태)

    userRepository.deleteById(userId);
  }
}