package com.spring.mission.discodeit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.util.BinaryContentUtils;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  //단위테스트: 비즈니스 로직이 원하는 결과를 도출하는지 <-확인!
  //@Mock으로 외부 의존성 대체 ,
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusService userStatusService;
  @Mock
  private UserMapper userMapper;

  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentUtils binaryContentUtils;

  @InjectMocks
  private BasicUserService userService;
  //given
  // when ..  thenReturn
  //verity -> 어떤 메서드가 몇 번/어떤 파라미터로 호출되었는지 확인 가능

  @Nested
  @DisplayName("유저생성")
  class UserCreate {

    private UserRequest request;
    private User user;
    private UserDto userDto;


    @BeforeEach
    void setUp() {
      request = new UserRequest(
          "testUser", "1234", "test@test.com"
      );
      user = User.builder()
          .username("testUser")
          .password("1234")
          .email("test@test.com")
          .profile(null).build();
      userDto = UserDto.builder()
          .id(UUID.randomUUID())
          .name("testUser")
          .email("test@test.com")
          .profile(null)
          .online(true)
          .build();

    }

    @Test
    @DisplayName("유저생성 성공")
    void testCreateUser() {
      // Given 준비, mock의 메서드가 가지고 있는 다른 의존성을 가진 객체의 메서드 동작을 설정한다.
      given(userRepository.existsByEmail(request.email())).willReturn(false);
      given(userRepository.existsByUsername(request.name())).willReturn(false);
      given(userRepository.save(any(User.class))).willReturn(user);
      given(userMapper.toDto(user)).willReturn(userDto);
      given(binaryContentUtils.makeNullableProfile(Optional.empty()))
          .willReturn(null);

      // When - 실행
      UserDto result = userService.createUser(request, Optional.empty());

      // Then - 결과
      assertEquals(result, userDto);
      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo(request.name());
      assertThat(result.getEmail()).isEqualTo(request.email());
      then(userRepository).should().save(any(User.class));
      then(userStatusService).should().create(any(UserStatusRequest.class));
    }

    @Test
    @DisplayName("실패-중복 이름")
    void testCreateUserFailedExistsName() {
      //given
      given((userRepository.existsByUsername(request.name()))).willReturn(true);

      //when
      assertThrows(UserAlreadyExistsException.class, () -> {
        userService.createUser(request,
            Optional.empty());
      });

      //then
      then(userRepository).should(never()).save(any(User.class));
      then(userStatusService).should(never()).create(any(UserStatusRequest.class));
    }

    @Test
    @DisplayName("실패-중복 이메일")
    void testCreateUserFailedExistsEmail() {

      // Given 준비, mock의 메서드가 가지고 있는 다른 의존성을 가진 객체의 메서드 동작을 설정한다.
      given((userRepository.existsByUsername(request.name()))).willReturn(false);
      given(userRepository.existsByEmail(request.email())).willReturn(true);
      // When & Then - 예외 발생 검증
      assertThrows(UserAlreadyExistsException.class, () -> {
        userService.createUser(request, Optional.empty());
      });

      // 로그 찍히는 정도까지는 테스트하지 않음 (보통 로그는 단위 테스트 대상 아님)
      then(userRepository).should(never()).save(any(User.class)); // 저장되지 않아야 함
      then(userStatusService).shouldHaveNoInteractions(); // 상태도 생성되면 안 됨

    }

  }

  @Nested
  @DisplayName("유저 업데이트")
  class UserUpdate {

    private UserUpdateRequest userUpdateRequest;
    private User existingUser;
    private UserDto userDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
      userUpdateRequest = new UserUpdateRequest("newUserName", "newPassword",
          "newEmail@example.com");
      userId = UUID.randomUUID();
      existingUser = User.builder()
          .username("oldUserName")
          .email("oldEmail@example.com")
          .password("oldPassword")
          .profile(null)  // Assuming no profile initially
          .build();
      userDto = UserDto.builder()
          .id(userId)
          .name("newUserName")
          .email("newEmail@example.com")
          .profile(null)
          .online(true)
          .build();

    }

    @Test
    @DisplayName("성공")
    void testUpdateUser() {
      // Given
      given(userRepository.findById(userId)).willReturn(
          Optional.of(existingUser));  // 기존 유저 반환
      given(binaryContentUtils.makeNullableProfile(Optional.empty())).willReturn(
          null);  // 이미지 변경없음
      given(userRepository.save(existingUser)).willReturn(
          existingUser);  // 유저 저장
      given(userMapper.toDto(existingUser)).willReturn(userDto);  // Convert to DTO

      // When
      UserDto result = userService.updateUser(userId, userUpdateRequest, Optional.empty());

      // Then
      assertEquals("newUserName", result.getName());  // Assert the updated username
      assertEquals("newEmail@example.com", result.getEmail());  // Assert the updated email
      assertNull(result.getProfile());  // No profile update, so it should be null
      then(userRepository).should().save(existingUser);  // 유저 저장 확인
      then(binaryContentUtils).should(never())
          .deleteBinaryContentByUserId(userId);  // 이미지 변경x니까 기존 이미지 삭제 일어나지 않음.
    }

    @Test
    @DisplayName("실패-존재하는 이름")
    void testUpdateUserFailedExistsName() {
      //given
      given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
      given(binaryContentUtils.makeNullableProfile(Optional.empty())).willReturn(null);
      given(userRepository.existsByUsername(userUpdateRequest.newName())).willReturn(true);

      //when
      assertThrows(UserAlreadyExistsException.class, () -> {
        userService.updateUser(userId, userUpdateRequest, Optional.empty());
      });

      //then
      then(userRepository).should(never()).save(any(User.class));

    }

  }

  @Nested
  @DisplayName("유저 삭제")
  class UserDelete {

    private UserRequest request;
    private User user;
    private UUID uuid;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
      request = new UserRequest(
          "testUser", "1234", "test@test.com"
      );
      uuid = UUID.randomUUID();
      user = User.builder()
          .username("testUser")
          .password("1234")
          .email("test@test.com")
          .profile(null).build();
      userDto = UserDto.builder()
          .id(uuid)
          .name("testUser")
          .email("test@test.com")
          .profile(null)
          .online(true)
          .build();

    }

    @Test
    @DisplayName("성공")
    void testDeleteUser() {
      //given
      given(userRepository.findById(uuid)).willReturn(Optional.of(user));

      //when
      userService.deleteUser(uuid);

      //then
      then(binaryContentUtils).should().deleteBinaryContentByUserId(uuid);
      then(userRepository).should().deleteById(uuid);
    }

    @Test
    @DisplayName("실패-유저없음")
    void testDeleteUserFailed() {
      given(userRepository.findById(uuid)).willReturn(Optional.empty());

      assertThrows(UserNotFoundException.class, () -> {
        userService.deleteUser(uuid);
      });

      then(binaryContentUtils).should(never()).deleteBinaryContentByUserId(uuid);
      then(userRepository).should(never()).deleteById(uuid);


    }

  }

}
