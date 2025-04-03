package com.sprint.mission.discodeit.service.basic;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserDuplicateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

//단위 테스트를 바라보는 관점
//
@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @InjectMocks
  private BasicUserService basicUserService;

  @DisplayName("프로필 사진을 가진 유저를 생성할 수 있다.")
  @Test
  void create() {
    //given
    UserCreateDTO dto = new UserCreateDTO("testName", "test@abc.com", "password");

    byte[] bytes = new byte[]{1, 2, 3};
    BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest(
        "profile.jpg", "image/jpeg", bytes);

    Optional<BinaryContentCreateRequest> optionalProfile = Optional.of(profileRequest);

    //내부 저장 객체는 mock (id 모른다, 흐름만 검증)
    BinaryContent saveProfile = new BinaryContent("profile.jpg", "image/jpeg", 3L);
    User saveUser = new User(dto.getUsername(), dto.getEmail(), dto.getPassword(), saveProfile);

    UserDto expectedDto = new UserDto(null, dto.getUsername(), dto.getEmail(), null, true);

    given(userRepository.existsByEmail(dto.getEmail())).willReturn(false);
    given(userRepository.existsByUsername(dto.getUsername())).willReturn(false);
    given(binaryContentRepository.save(any())).willReturn(saveProfile);
    given(userRepository.save(any())).willReturn(saveUser);
    given(userMapper.toDto(saveUser)).willReturn(expectedDto);

    //when
    UserDto result = basicUserService.create(dto, optionalProfile);

    //then
    assertNotNull(result);
    assertEquals(dto.getUsername(), result.getUsername());
    assertEquals(dto.getEmail(), result.getEmail());

    verify(userRepository).existsByEmail(dto.getEmail());
    verify(userRepository).existsByUsername(dto.getUsername());
    verify(binaryContentRepository).save(any(BinaryContent.class));
    verify(binaryContentStorage).put(any(), eq(bytes));
    verify(userRepository).save(any(User.class));
    verify(userMapper).toDto(any(User.class));

  }

  @DisplayName("이메일이 중복인 경우 유저를 생성할 수 없다.")
  @Test
  void emailDuplication_throws() {
    //given
    UserCreateDTO dto = new UserCreateDTO("testName", "test@abc.com", "password");
    given(userRepository.existsByEmail("test@abc.com")).willReturn(true);

    //when //then
    assertThrows(UserDuplicateException.class,
        () -> basicUserService.create(dto, Optional.empty()));

    verify(userRepository).existsByEmail("test@abc.com");
    verify(userRepository, never()).save(any());
  }

  @DisplayName("이름이 중복인 경우 유저를 생성할 수 없다.")
  @Test
  void usernameDuplication_throws() {
    //given
    UserCreateDTO dto = new UserCreateDTO("testName", "test@abc.com", "password");
    given(userRepository.existsByUsername("testName")).willReturn(true);

    //when //then
    assertThrows(UserDuplicateException.class,
        () -> basicUserService.create(dto, Optional.empty()));

    verify(userRepository).existsByUsername("testName");
    verify(userRepository, never()).save(any());
  }

  @DisplayName("사용자의 이름, 이메일, 비밀번호, 프로필 사진을 정보를 수정할 수 있다.")
  @Test
  void update() {
    //given
    UUID userId = UUID.randomUUID();
    UserUpdateDTO dto = new UserUpdateDTO("newUsername", "new@abc.com", "new1234");

    byte[] bytes = new byte[]{1, 2, 3};
    BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest("img.jpg",
        "image/jpeg", bytes);
    Optional<BinaryContentCreateRequest> optionalProfile = Optional.of(profileRequest);

    User existingUser = new User("oldName", "old@abc.com", "old1234", null);
    given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
    given(userRepository.existsByEmail("new@abc.com")).willReturn(false);
    given(userRepository.existsByUsername("newUsername")).willReturn(false);

    BinaryContent saveProfile = new BinaryContent("img.jpg", "image/jpeg", 3L);
    given(binaryContentRepository.save(any())).willReturn(saveProfile);

    BinaryContentDto profileDto = new BinaryContentDto(null, "img.jpg", 3L, "image/jpeg");
    UserDto expectedDto = new UserDto(null, dto.getNewUsername(), dto.getNewEmail(), profileDto,
        true);
    given(userMapper.toDto(existingUser)).willReturn(expectedDto);

    //when
    UserDto result = basicUserService.update(userId, dto, optionalProfile);

    //then
    assertNotNull(result);
    assertNotNull(result.getProfile());
    assertEquals("img.jpg", result.getProfile().getFileName());
    assertEquals("newUsername", result.getUsername());
    assertEquals("new@abc.com", result.getEmail());

    verify(userRepository).findById(userId);
    verify(userRepository).existsByUsername("newUsername");
    verify(userRepository).existsByEmail("new@abc.com");
    verify(binaryContentRepository).save(any());
    verify(binaryContentStorage).put(any(), eq(bytes));
    verify(userMapper).toDto(existingUser);
  }

  @DisplayName("유저를 찾을 수 없다면 업데이트를 할 수 없다.")
  @Test
  void userNotFound_updateFail() {
    //given
    UUID userId = UUID.randomUUID();
    UserUpdateDTO dto = new UserUpdateDTO("newUsername", "new@abc.com", "new1234");
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    //when //then
    assertThrows(UserNotFoundException.class,
        () -> basicUserService.update(userId, dto, Optional.empty()));
  }

  @DisplayName("사용자를 삭제할 수 있다")
  @Test
  void delete() {
    //given
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(true);

    //when
    basicUserService.delete(userId);

    //then
    verify(userRepository).existsById(userId);
    verify(userRepository).deleteById(userId);
  }

  @DisplayName("사용자가 존재하지 않은경우 삭제할 수 없다.")
  @Test
  void userNotFound_deleteFail() {
    //given
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(false);

    //when //then
    assertThrows(UserNotFoundException.class, () -> basicUserService.delete(userId));
    verify(userRepository).existsById(userId);
    verify(userRepository, never()).deleteById(userId);
  }


}