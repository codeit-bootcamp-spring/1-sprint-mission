package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
// final/nonnull 이라 값이 반드시 있어야 하는 생성자 자동 생성, @Autowired 안써줘도 생성자가 하나일 때는 자동으로 의존성 주입 (+생성자 있으면 자바단에서 기본생성자 생성 안함)
@Service
public class BasicUserService extends UserMapper implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;


  @Transactional
  @Override
  // TODO : 회원이 입력할 수 있는 값만 따로 dto로 묶어 전달
  public UserDto create(UserCreateRequest request,
      Optional<MultipartFile> nullableFile) {
    // TODO : 1. 발생 가능한 예외를 먼저 생각하기 -> 예외 처리 : email과 username 검색 => 이존회
    // TODO : 2. dto -> 변수 할당 3. 객체 생성 4. repository.save
    String email = request.email();
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 존재하는 회원입니다.");
      // TODO : '예외 처리'라는 건 예외를 해결하는 게 아니라, 프로그램이 종료되지 않도록 예외 상황을 출력/상태코드 반환해서 알리는 게 목적임. 예외 해결은 예외 처리 개념이 아니라 그냥 애초에 예외를 방지하는 로직을 쓰는 것으로 수행.
      // TODO : 예외 발생 -> 메인/Controller 단에서 처리 안하면 JVM으로 넘어가서 프로그램 종료될 수 있음 -> ExceptionHandler에서 처리해주기
      // TODO : @ControllerAdive, @ExceptionHandler
    }

    // 프로필 이미지 설정
    // TODO : 이해 1. .map의 기능, 문법 2. Optional 문법 -> ok
    // TODO : 프로필 정보를 받았어 그런데 회원 객체 생성시 프로필 객체가 필요해 -> 프로필을 겟하는 로직을 써야겠네
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
    binaryContentStorage.put(profile.getId(), data);

    String username = request.username();
    String password = request.password();
    User newUser = User.builder()
        .username(username)
        .email(email)
        .password(password)
        .profile(profile)
        .build();
    User user = userRepository.save(newUser);
    UserDto createdUser = toDto(user);

    // 생성된 회원 user status 설정
    Instant lastActiveAt = Instant.now();
    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(lastActiveAt)
        .build();
    userStatusRepository.save(userStatus);
    return createdUser;
  }

  @Override
  // TODO : 회원을 찾고 반환하는 값으로 password를 주진 않을 거잖아 -> 보안 차원의 DTO
  public UserDto find(UUID userId) {
    // 반환타입이 UserDto니까, mapper을 이용해 Optional의 null이 아닌 경우인 user 변수를 dto로 변경해서 return
    return userRepository.findById(userId)
        .map(user -> toDto(user))
        .orElseThrow(() -> new NoSuchElementException("아이디가" + userId + "인 회원이 존재하지 않습니다."));
  }

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
        .orElseThrow(() -> new NoSuchElementException("아이디가 " + userId + "인 회원이 존재하지 않습니다."));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      throw new IllegalArgumentException("이메일이 " + newEmail + "인 회원이 이미 존재합니다.");
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new IllegalArgumentException("이름이 " + newUsername + "인 회원이 이미 존재합니다.");
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

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, newProfile);

    return toDto(user);
  }


  @Transactional
  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("아이디가 " + userId + "인 회원이 존재하지 않습니다."));

    Optional.ofNullable(user.getProfile()).map(BinaryContent::getId)
        .ifPresent(
            binaryContentRepository::deleteById); // BinaryContent::getId는 BinaryContent 객체가 존재하면 그 객체의 getId() 메소드를 호출하는 것
    userStatusRepository.deleteByUserId(userId); // 관련 도메인 삭제 (프로필, 회원상태)

    userRepository.deleteById(userId);
  }
}