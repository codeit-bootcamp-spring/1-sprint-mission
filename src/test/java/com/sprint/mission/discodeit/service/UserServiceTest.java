package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentService binaryContentService;

  @Mock
  private UserStatusService userStatusService;

  @Mock
  private UserMapper userMapper;

  @Mock
  BinaryContentMapper binaryContentMapper;

  @Mock
  InputHandler inputHandler;

  @InjectMocks
  private BasicUserService basicUserService;

  /**
   * 유저 생성
   **/
  // 성공
  @Test
  void createUser_Success() {
    /** given **/
    // (1) UserService 에서 User 객체 생성 시도할 때 받는 파라미터
    UserCreateRequest userCreateRequest = new UserCreateRequest("testUser", "test@example.com",
        "password123");
    Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.of(
        new BinaryContentCreateRequest(
            "profile.jpg", 1024L, "image/jpeg", new byte[]{}));

    // (2) 유효성 검증 -- when이 실행될 때 아래와 같은 결과를 반환한다.
    given(userRepository.existsByUsername("testUser")).willReturn(false);
    given(userRepository.existsByEmail("test@example.com")).willReturn(false);

    // (3) UserRepository 에서 객체 저장 후의 반환값을 위해 User 객체 생성
    User user = User.builder()
        .username("testUser")
        .email("test@example.com")
        .password("password123")
        .build();

    // (3-1) UserRepository 에서 객체 저장 시도 성공 후 user 을 반환한다.
    given(userRepository.save(any(User.class))).willReturn(user);

    // todo : getId()
    // (4) userMapper.toDto() 메서드를 호출하면 아래를 반환한다.
    given(userMapper.toDto(any(User.class))).willReturn(new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        null,
        true
    ));

    /** when -- userService 에서 createUser() 메서드를 호출할 때 **/
    UserDto result = basicUserService.createUser(userCreateRequest, binaryContentCreateRequest);

    /** then **/
    // userRepository.existsByUsername("testUser")가 한 번 호출되었는지 검증
    then(userRepository).should().existsByUsername("testUser");
    // 이하 동문
    then(userRepository).should().existsByEmail("test@example.com");
    then(binaryContentRepository).should().save(any(BinaryContent.class));
    then(binaryContentStorage).should()
        .put(any(UUID.class), binaryContentCreateRequest.get().bytes());

    // 클라이언트가 요청해서 그대로 전달되는 객체 --> 특정한 객체가 전달되었는지 확인하기 위해
    // 서비스 내부에서 새로 생성되는 객체 --> 어떤 객체든 전달되었는지만 확인하기 위해 any(...)
    then(userStatusService).should().createUserStatus(any(UserStatusCreateRequest.class));
    then(userRepository).should().save(any(User.class));
    then(userMapper).should().toDto(any(User.class));

    // junit5 쥬피터의 검증 메서드
    // 결과가 null 인지 아닌지 검증
    assertNotNull(result);
    // 반환한 UserDto가 기대한 값과 일치하는지 검증
    assertEquals("testUser", result.username());
    assertEquals("test@example.com", result.email());
  }

  // 실패 : 사용자명이 이미 존재하는 경우
  @Test
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

    then(userStatusService).should(never()).createUserStatus(any(UserStatusCreateRequest.class));
    then(userMapper).should(never()).toDto(any(User.class));
  }

  /**
   * 유저 수정
   **/

  // 성공
  @Test
  void updateUser_Success() {
    /** given **/
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
        "newUserName",
        "newEmail@example.com",
        "newPassword123");
    Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.of(
        new BinaryContentCreateRequest(
            "profile.jpg", 1024L, "image/jpeg", new byte[]{}));

    // userRepository.findById() 메서드 호출시 existingUser를 반환한다.
    // (1) 기존 User 객체 반환
    User existingUser = User.builder()
        .username("oldUsername")
        .email("oldemail@example.com")
        .password("oldPassword")
        .build();

    UUID userId = existingUser.getId();

    given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));

    // (2) 유효성 검증
    given(userRepository.existsByUsername("newUserName")).willReturn(false);
    given(userRepository.existsByEmail("newEmail@example.com")).willReturn(false);

    // (3) 프로필 이미지 생성 성공 시 반환 값 설정

    UUID profileId = UUID.randomUUID();

    // (4) usreMapper.toDto() 메서드가 호출될 때 Userdto 설정
    // 파라미터로 받지 않는 값들은 any() 로 지정하기
    given(userMapper.toDto(any(User.class))).willReturn(new UserDto(
        userId,
        "newUserName",
        "newEmail@example.com",
        new BinaryContentDto(
            profileId,
            "newProfile.png",
            1340L,
            "image/png",
            new byte[]{}
        ),
        true));

    /**when**/
    UserDto result = basicUserService.updateUserInfo(userId, userUpdateRequest,
        binaryContentCreateRequest);

    /**then**/
    then(userRepository).should().findById(userId);
    then(userRepository).should().existsByUsername("newUserName");
    then(userRepository).should().existsByEmail("newEmail@example.com");

    then(userMapper).should().toDto(existingUser);

    assertNotNull(result);
    assertEquals("newUserName", result.username());
    assertEquals("newEmail@example.com", result.email());
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
    then(userStatusService).should().delteUserStatusByUserId(user.getId());
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
    then(userStatusService).should(never()).deleteUserStatusById(user.getId());
    then(binaryContentService).should(never()).deleteBinaryContentById(any());
    then(userRepository).should(never()).deleteById(deleteId);
  }
}
