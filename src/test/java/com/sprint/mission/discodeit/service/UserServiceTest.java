package com.sprint.mission.discodeit.service;


import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserDto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.status.AccountStatus;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayName("사용자 서비스 테스트")
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private BinaryContentService binaryContentService;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService userService;

  @Nested
  @DisplayName("사용자 생성 테스트")
  class createTest {

    @Test
    @DisplayName("프로필 없는 사용자 생성 성공")
    void createUser() {
      // given
      CreateUserDto createUserDto = new CreateUserDto(
          "test",
          "테스트",
          "test@discodeit.com",
          "test1234",
          "테스트 유저 입니다."
      );

      //가짜 user 객체 생성

      UUID userId = UUID.randomUUID();

      User user = new User();
      user.setUsername("test");
      user.setEmail("test@discodeit.com");
      user.setPassword("test1234");

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);
      } catch (Exception e) {
        fail("Failed to set ID using reflection: " + e.getMessage());
      }

      UserDto expectedUserDto = new UserDto(
          userId,
          "test",
          "test@discodeit.com",
          true,
          null
      );
      UserStatus userStatus = new UserStatus(user);

      // Mock: 가짜 user 객체 반환하도록 설정
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(userMapper.toDto(any(User.class))).thenReturn(expectedUserDto);
      when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);

      // when
      UserDto userDto = userService.create(createUserDto);

      //then
      Assertions.assertEquals(userDto.email(), user.getEmail());

      // userRepository.save가 정확히 한 번 호출되었는지 검증
      verify(userRepository, times(1)).save(any(User.class));
      verify(userMapper, times(1)).toDto(any(User.class)); // userMapper가 호출되었는지 확인

    }


    @Test
    @DisplayName("중복 이메일 예외 발생")
    void createUserFailureEmailAlreadyExist() {

      // given
      CreateUserDto createUserDtoDuplicate = new CreateUserDto(
          "test_1",
          "테스트_1",
          "test@discodeit.com",
          "test12345678",
          "테스트 유저2 입니다."
      );

      //가짜 user 객체 생성

      UUID userId = UUID.randomUUID();

      User user = new User();
      user.setUsername("test");
      user.setEmail("test@discodeit.com");
      user.setPassword("test1234");

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);
      } catch (Exception e) {
        fail("Failed to set ID using reflection: " + e.getMessage());
      }

      UserDto expectedUserDto = new UserDto(
          userId,
          "test",
          "test@discodeit.com",
          true,
          null
      );

      // Mock: 가짜 user 객체 반환하도록 설정
      when(userRepository.findByEmail("test@discodeit.com")).thenReturn(Optional.of(user));

      // when & Then
      Assertions.assertThrows(UserAlreadyExistException.class,
          () -> userService.create(createUserDtoDuplicate));

      // userRepository.save가 정확히 한 번 호출되었는지 검증
      verify(userRepository, never()).save(any(User.class));
      verify(userRepository, times(1)).findByEmail("test@discodeit.com");
      verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    @DisplayName("중복 Username 예외 발생")
    void createUserFailureUserNameAlreadyExist() {

    }

    @Test
    @DisplayName("프로필 이미지와 함께 생성")
    void createUserWithProfile() {
      // given
      // createUserDto
      CreateUserDto createUserDto = new CreateUserDto(
          "test",
          "테스트",
          "test@discodeit.com",
          "test1234",
          "테스트 유저 입니다."
      );

      //MultipartFile
      MultipartFile multipartFile = new MockMultipartFile("file", "img.png", "img/png",
          "test".getBytes());
      //가짜 user 객체 생성

      UUID userId = UUID.randomUUID();

      User user = new User();
      user.setUsername("test");
      user.setEmail("test@discodeit.com");
      user.setPassword("test1234");

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);
      } catch (Exception e) {
        fail("Failed to set ID using reflection: " + e.getMessage());
      }

      //가짜 BinaryContent, BinaryContentDto 객체 생성
      UUID binaryId = UUID.randomUUID();

      BinaryContent binaryContent = new BinaryContent(
          multipartFile.getName(),
          multipartFile.getContentType(),
          multipartFile.getSize()
      );

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(binaryContent, binaryId);
      } catch (Exception e) {
        fail("Failed to set BinaryContent ID using reflection: " + e.getMessage());
      }

      BinaryContentDto binaryContentDto = new BinaryContentDto(
          binaryContent.getId(),
          binaryContent.getFileName(),
          binaryContent.getContentType(),
          binaryContent.getCreatedAt(),
          binaryContent.getSize()
      );

      // 예상되는 반환 UserDto
      UserDto expectedUserDto = new UserDto(
          userId,
          "test",
          "test@discodeit.com",
          true,
          // 프로필 이미지는 null,
          binaryContentDto
      );

      // Mock: 가짜 user 객체 반환하도록 설정
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(userMapper.toDto(any(User.class))).thenReturn(expectedUserDto);

      // when
      UserDto userDto = userService.create(createUserDto);

      //then
      Assertions.assertAll(
          () -> Assertions.assertEquals(userDto.email(), user.getEmail()),
          () -> Assertions.assertEquals(userDto.profile().id(), binaryId)
      );

      // userRepository.save가 정확히 한 번 호출되었는지 검증
      verify(userRepository, times(1)).save(any(User.class));
      verify(userMapper, times(1)).toDto(any(User.class)); // userMapper가 호출되었는지 확인

    }
  }

  @Nested
  @DisplayName("사용자 수정 테스트")
  class updateUser {

    private CreateUserDto createUserDto;
    private UUID userId;
    private User user;
    private UserDto expectedUserDto;
    private UserStatus userStatus;

    @BeforeEach
    void setUp() {

      // 가짜 user 객체 생성
      userId = UUID.randomUUID();

      user = new User();
      user.setUsername("test");
      user.setEmail("test@discodeit.com");
      user.setPassword("test1234");

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);
      } catch (Exception e) {
        fail("Failed to set ID using reflection: " + e.getMessage());
      }

      expectedUserDto = new UserDto(
          userId,
          "test",
          "test@discodeit.com",
          true,
          null
      );

      userStatus = new UserStatus(user);
    }

    @Test
    void updateUserSuccess() {
      UpdateUserDto updateUserDto = new UpdateUserDto(
          "new name",
          "1234",
          "new_test@discodeit.com",
          true,
          null,
          Instant.now()
      );

      // 사용자 상태 모킹
      //userRepository 가 findById를 사용한다면, user를 return
      when(userRepository.findById(userId)).thenReturn(Optional.of(user));
      //userStatusRepository가 어떤 객체든 User 클래스에 속하는 객체를 파라미터로 findByUser를 사용한다면,userStatus 를 Return
      when(userStatusRepository.findByUser(any(User.class))).thenReturn(Optional.of(userStatus));
      // userRepository가 save를 어떤 객체든, User 클래스인 객체로 save를 한다면, user를 return
      when(userRepository.save(any(User.class))).thenReturn(user);

      // 업데이트된 DTO
      UserDto updatedDto = new UserDto(
          userId,
          "new name",
          "new_test@discodeit.com",
          true,
          null
      );

      when(userMapper.toDto(any(User.class))).thenReturn(updatedDto);

      //when
      UserDto result = userService.updateUser(userId.toString(), updateUserDto);

      // then
      Assertions.assertAll(
          () -> Assertions.assertNotNull(result),
          () -> Assertions.assertEquals("new name", result.username()),
          () -> Assertions.assertEquals("new_test@discodeit.com", result.email())
      );

      verify(userRepository, times(1)).findById(userId);
      verify(userStatusRepository, times(1)).findByUser(any(User.class));
      verify(userRepository, times(2)).save(any(User.class));
      verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void updateUserFailure() {

    }
  }

  @Nested
  @DisplayName("사용자 삭제 테스트")
  class deleteUser {

    private CreateUserDto createUserDto;
    private UUID userId;
    private User user;
    private UserDto expectedUserDto;
    private UserStatus userStatus;

    @BeforeEach
    void setUp() {

      // 가짜 user 객체 생성
      userId = UUID.randomUUID();

      user = new User();
      user.setUsername("test");
      user.setEmail("test@discodeit.com");
      user.setPassword("test1234");

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);
      } catch (Exception e) {
        fail("Failed to set ID using reflection: " + e.getMessage());
      }

      expectedUserDto = new UserDto(
          userId,
          "test",
          "test@discodeit.com",
          true,
          null
      );

      userStatus = new UserStatus(user);
      user.setStatus(userStatus);

    }


    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUserSuccess() {

      //when
      when(userRepository.findById(userId)).thenReturn(Optional.of(user));

      //then
      Assertions.assertTrue(userService.deleteUser(userId.toString()));

      verify(userRepository, times(1)).findById(userId);
      verify(userRepository, times(1)).delete(any(User.class));

    }

    @Test
    @DisplayName("사용자 삭제 실패")
    void deleteUserFailure() {

      //given
      UUID randomUUID = UUID.randomUUID();

      //when
      when(userRepository.findById(randomUUID)).thenReturn(Optional.empty()); // 사용자를 찾지 못함

      //then
      Assertions.assertThrows(UserNotFoundException.class,
          () -> userService.deleteUser(randomUUID.toString()));

      verify(userRepository, times(1)).findById(randomUUID);
      verify(userRepository, never()).delete(any(User.class));

    }

  }
}
