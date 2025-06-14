package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * TDD             -> BDD 이용 when.thenReturn -> given.willRetrun verity          -> then.sould()
 **/

/**
 * create, update, delete 메소드 2개 이상(성공, 실패)의 테스트 케이스
 **/

@ExtendWith(MockitoExtension.class) // Junit5 + mockito
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BinaryContentService binaryContentService;

  @Mock
  private UserMapper userMapper;

  @Mock
  BinaryContentMapper binaryContentMapper;

  @Mock
  InputHandler inputHandler;

  @InjectMocks
  private BasicUserService basicUserService;

  private UUID userId;
  private String username;
  private String email;
  private String password;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    username = "testUser";
    email = "test@example.com";
    password = "password123";

    Role role = new Role("ROLE_USER");

    user = User.builder()
        .username(username)
        .email(email)
        .password(password)
        .build();
    ReflectionTestUtils.setField(user, "id", userId); // private, final 필드 강제 값 주입

    userDto = new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        null,
        Set.of(role)
    );
  }


  /**
   * 유저 생성
   **/
  // 성공
  @Test
  @DisplayName("사용자 생성 성공")
  void createUser_Success() {
    /** given **/
    // (1) UserService 에서 User 객체 생성 시도할 때 받는 파라미터
    UserCreateRequest userCreateRequest = new UserCreateRequest(username, email, password);

    given(userRepository.existsByUsername(eq(username))).willReturn(false);
    given(userRepository.existsByEmail(eq(email))).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    /** when -- userService 에서 createUser() 메서드를 호출할 때 **/
    UserDto result = basicUserService.createUser(userCreateRequest, Optional.empty());

    /** then **/
    then(userRepository).should().save(any(User.class));
    assertThat(result).isEqualTo(userDto);
    // 반환한 UserDto가 기대한 값과 일치하는지 검증
    assertEquals("testUser", result.username());
    assertEquals("test@example.com", result.email());
  }

  // 실패 : 사용자명이 이미 존재하는 경우
  @Test
  @DisplayName("사용자 생성 실패")
  void createUser_Fail_UsernameAlreadyExists() {
    /** given **/
    // (1) UserService 에서 User 객체 생성 시도할 때 받는 파라미터
    UserCreateRequest userCreateRequest = new UserCreateRequest("existingUser", "test@example.com",
        "password123");
    Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.of(
        new BinaryContentCreateRequest(
            "profile.jpg", 1024L, "image/jpeg", new byte[]{}));

    // (2) 이미 존재하는 사용자명 시뮬레이션
    given(userRepository.existsByUsername("existingUser")).willReturn(true);

    /** when & then - 예외 발생 검증 **/
    assertThrows(UsernameAlreadyExistsException.class, () -> {
      basicUserService.createUser(userCreateRequest, binaryContentCreateRequest);
    });

    // 예외를 발생시킬 메서드가 호출되었는지 검증
    then(userRepository).should().existsByUsername("existingUser");

    // 아래 메서드들이 발생되지 않았는지 검증
    then(userRepository).should(never()).existsByEmail("test@example.com");
    then(userRepository).should(never()).save(any(User.class));

    then(userMapper).should(never()).toDto(any(User.class));
  }

  /**
   * 유저 수정
   **/
  // 성공
  @Test
  @DisplayName("사용자 수정 성공")
  void updateUser_Success() {
    /** given **/
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
        "newUserName",
        "newEmail@example.com",
        "newPassword123");

    User updateUser = User.builder()
        .username("newUserName")
        .email("newEmail@example.com")
        .password("newPassword123")
        .build();

    Role role = new Role("ROLE_USER");

    UserDto updateUserDto = new UserDto(
        updateUser.getId(),
        updateUser.getUsername(),
        updateUser.getEmail(),
        null,
        Set.of(role)
    );

    // userRepository.findById() 메서드 호출시 existingUser를 반환한다.
    given(userRepository.findById(any(UUID.class))).willReturn(Optional.ofNullable(user));
    // (2) 유효성 검증
    given(userRepository.existsByUsername("newUserName")).willReturn(false);
    given(userRepository.existsByEmail("newEmail@example.com")).willReturn(false);
    /** +++++ 멘토님! 이 부분 +++++ (검증이랑 연계해서 봐주세요) **/
    // given(userMapper.toDto(any(User.class))).willReturn(userDto);
    given(userMapper.toDto(any(User.class))).willReturn(updateUserDto);

    /**when**/
    UserDto result = basicUserService.updateUserInfo(userId, userUpdateRequest,
        Optional.empty());

    /**then**/
    /** +++++
     * 베이스 코드에서 이런 식으로 되어있던데
     * 이러면 테스트의 의미가 없지 않나요!?
     * (내부적으로 바뀌는지는 중요하지 않고 userDto 반환하는 걸 받아가는 거라서)
     * +++++
     * **/
    // assertThat(result).isEqualTo(userDto);
    assertThat(result.username()).isEqualTo("newUserName");
    assertThat(result.email()).isEqualTo("newEmail@example.com");
  }

  // 실패 : 유저를 찾지 못했을 때
  @Test
  void updateUser_Fail_UserNotFound() {
    /**given**/
    UUID notFoundId = UUID.randomUUID();
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
        "newUserName",
        "newEmail@example.com",
        "newPassword123");
    Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.of(
        new BinaryContentCreateRequest(
            "profile.jpg", 1024L, "image/jpeg", new byte[]{}));

    // (1) 존재하지 않는 사용자에 대한 시뮬레이션
    given(userRepository.findById(notFoundId)).willReturn(Optional.empty());

    /** when & then **/
    assertThrows(UserNotFoundException.class, () -> {
      basicUserService.updateUserInfo(notFoundId, userUpdateRequest, binaryContentCreateRequest);
    });

    // 예외를 발생시킬 메서드가 호출되었는지 검증
    then(userRepository).should().findById(notFoundId);

    // 아래 메서드들이 발생되지 않았는지 검증
    then(userRepository).should(never()).existsByUsername("newUserName");
    then(userRepository).should(never()).existsByEmail("newEmail@example.com");

    then(userMapper).should(never()).toDto(any(User.class));
    then(binaryContentMapper).should(never()).toEntity(any(BinaryContentDto.class));

  }

  /**
   * 유저 삭제
   **/
  // 성공
  @Test
  void deleteUser_Success() {
    /**given**/

    // (1) inputHandler.getYesNOInput() 메서드 호출시 y 반환
    given(inputHandler.getYesNOInput()).willReturn("y");

    // 프로필 이미지 삭제를 위해 생성
    BinaryContent profile = new BinaryContent(
        "newProfile.png",
        1340L,
        "image/png"
    );
    // (2) userRepository.findById() 메서드 호출시 user 를 반환한다.
    User user = User.builder()
        .username("username")
        .email("email@example.com")
        .password("Password123")
        .profile(profile)
        .build();

    UUID deleteId = user.getId();
    given(userRepository.findById(deleteId)).willReturn(Optional.of(user));

    /**when**/
    basicUserService.removeUserById(deleteId);

    /**then**/
    // void 반환들은 호출되는 것만 확인한다.
    then(binaryContentService).should().deleteBinaryContentById(profile.getId());
    then(userRepository).should().deleteById(deleteId);
  }

  // 실패 : inputHandler.getYesNOInput()이 "n" 또는 다른 값 반환
  @Test
  void deleteUser_Fail_KeywordNotY() {
    /**given**/
    given(inputHandler.getYesNOInput()).willReturn("n");

    // (2) userRepository.findById() 메서드 호출시 user 를 반환한다.
    User user = User.builder()
        .username("username")
        .email("email@example.com")
        .password("Password123")
        .build();
    UUID deleteId = user.getId();

    // when
    basicUserService.removeUserById(deleteId);

    // then
    then(binaryContentService).should(never()).deleteBinaryContentById(any());
    then(userRepository).should(never()).deleteById(deleteId);
  }
}
