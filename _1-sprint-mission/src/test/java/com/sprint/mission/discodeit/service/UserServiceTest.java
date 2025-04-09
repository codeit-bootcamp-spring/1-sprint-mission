package com.sprint.mission.discodeit.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundExeption;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // Mockito와 JUnit5 를 연결
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @InjectMocks
  private BasicUserService userService;

  @Test
  @DisplayName("프로필이 없는 유저 생성")
  void testCreateUserWithoutProfile() {
    //given
    UserCreateRequest userCreateRequest = new UserCreateRequest(
        "username", "email@email.com", "Password123!"
    );
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

    User user = new User(
        "username", "email@email.com", "Password123!",
        null
    );
    //stub
    when(userRepository.save(any(User.class))).thenReturn(user);

    UUID fixedUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    UserDto expectedDto = new UserDto(
        fixedUuid, "username", "email@email.com", null, false
    );
    when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

    //when
    UserDto createdDto = userService.create(userCreateRequest, profileRequest);

    //then
    //반환값 검증
    assertEquals(expectedDto, createdDto);
    //userRepository.save() 호출 됐는지 검증
    verify(userRepository, times(1)).save(any(User.class));
    //usermapper 호출 됐는지 검증
    verify(userMapper, times(1)).toDto(any(User.class));

  }

  @Test
  @DisplayName("프로필이 있는 유저 생성")
  void testCreateUserWithProfile() {
    //given
    //사용자 생성 요청
    UserCreateRequest userCreateRequest = new UserCreateRequest(
        "username", "email@email.com", "Password123!"
    );
    //프로필 이미지 생성 요청
    BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
        "profile.png", "image/png", new byte[]{1, 2, 3}
    );
    Optional<BinaryContentCreateRequest> profileRequest = Optional.of(binaryContentCreateRequest);

    //예상되는 프로필 정보
    BinaryContent binaryContent = new BinaryContent(
        "profile.png",
        3L,
        "image/png"
    );
    when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);

    // 예상되는 User
    User user = new User(
        "username", "email@email.com", "Password123!", binaryContent
    );
    when(userRepository.save(any(User.class))).thenReturn(user);

    UUID fixedBinaryUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    BinaryContentDto binaryContentDto = new BinaryContentDto(
        fixedBinaryUuid, "profile.png", 3L, "image/png"
    );

    UUID fixedUserUuid = UUID.fromString("223e4567-e89b-12d3-a456-556642440000");
    UserDto expectedDto = new UserDto(fixedUserUuid, "username", "email@email.com",
        binaryContentDto, false);

    when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

    //when
    UserDto createdDto = userService.create(userCreateRequest, profileRequest);

    //then
    assertEquals(expectedDto, createdDto);
    verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
    verify(userRepository, times(1)).save(any(User.class));
    verify(userMapper, times(1)).toDto(any(User.class));
  }

  @Test
  @DisplayName("프로필이 있는 유저 생성 실패 - repository에서 잘못 반환")
  void testCreateUserWithProfileFail() {
    //given
    //사용자 생성 요청
    UserCreateRequest userCreateRequest = new UserCreateRequest(
        "username", "email@email.com", "Password123!"
    );
    //프로필 이미지 생성 요청
    BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
        "profile.png", "image/png", new byte[]{1, 2, 3}
    );
    Optional<BinaryContentCreateRequest> profileRequest = Optional.of(binaryContentCreateRequest);

    //예상되는 프로필 정보
    BinaryContent binaryContent = new BinaryContent(
        "profile.png",
        3L,
        "image/png"
    );
    when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);

    // repository가 잘못된 User 객체 반환
    User user = new User(
        "fail", "email@email.com", "Password123!", binaryContent
    );
    when(userRepository.save(any(User.class))).thenReturn(user);

    UUID fixedBinaryUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    BinaryContentDto binaryContentDto = new BinaryContentDto(
        fixedBinaryUuid, "profile.png", 3L, "image/png"
    );

    UUID fixedUserUuid = UUID.fromString("223e4567-e89b-12d3-a456-556642440000");
    UserDto expectedDto = new UserDto(fixedUserUuid, "username", "email@email.com",
        binaryContentDto, false);

    UserDto wrongDto = new UserDto(
        fixedUserUuid, "fail", "email@email.com",
        binaryContentDto, false
    );
    doReturn(wrongDto).when(userMapper).toDto(any(User.class));

    //when
    UserDto createdDto = userService.create(userCreateRequest, profileRequest);

    //then
    assertNotEquals(expectedDto, createdDto, "User 생성 실패되어야 함");
    verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
    verify(userRepository, times(1)).save(any(User.class));
    verify(userMapper, times(1)).toDto(any(User.class));
  }

  @Test
  @DisplayName("프로필 있는 유저 프로필 업데이트")
  void testUpdateUserWithProfile() {
    //given
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
        "newUsername", "newEmail@email.com", "newPassword123!"
    );
    BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
        "newProfile.png", "image/png", new byte[]{1, 2, 3}
    );
    Optional<BinaryContentCreateRequest> profileRequest = Optional.of(binaryContentCreateRequest);

    //기존 profile
    BinaryContent profile = new BinaryContent(
        "profile.png", 3L, "image/png"
    );
    //기존 User
    User user = new User(
        "user", "email@email.com", "Password!123", profile
    );

    UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    // 업데이트 될 새로운 프로필
    BinaryContent updatedProfile = new BinaryContent("newProfile.png", 3L, "image/png");

    //반환될 profiledto
    UUID profileId = UUID.fromString("123e4567-e89b-12d3-a456-556642440001");
    BinaryContentDto expectedProfileDto = new BinaryContentDto(
        profileId, "newProfile.png", 3L, "image/png"
    );
    // 반환될 userdto
    UserDto expectedUserDto = new UserDto(
        userId, "newUsername", "newEmail@email.com", expectedProfileDto, false
    );
    when(userMapper.toDto(any(User.class))).thenReturn(expectedUserDto);

    //when
    UserDto updatedDto = userService.update(userId, userUpdateRequest, profileRequest);

    //then
    assertEquals("newUsername", user.getUsername());
    assertEquals("newEmail@email.com", user.getEmail());
    assertEquals("newPassword123!", user.getPassword());
    assertEquals(expectedUserDto, updatedDto);
  }

  @Test
  @DisplayName("존재하지 않는 유저 업데이트")
  void testUpdateUserFail_UserNotFound() {
    //given
    UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
        "newUsername", "newEmail@email.com", "newPassword123!"
    );
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    //when, then
    UserNotFoundExeption exception = assertThrows(UserNotFoundExeption.class, () -> {
      userService.update(userId, userUpdateRequest, profileRequest);
    });
    // 예외의 ErrorCode가 USER_NOT_FOUND인지 확인
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @DisplayName("유저 삭제 성공")
  void testDeleteUser() {
    //given
    UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    when(userRepository.existsById(userId)).thenReturn(false);

    //when
    userService.deleteById(userId);

    //then
    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  @DisplayName("유저 삭제 실패 - 존재하지 않은 유저 삭제")
  void testDeleteUserFail_UserNotFound() {
    //given
    UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    when(userRepository.existsById(userId)).thenReturn(true);

    // when & then
    UserNotFoundExeption exception = assertThrows(UserNotFoundExeption.class, () -> {
      userService.deleteById(userId);
    });
    // 예외의 ErrorCode가 USER_NOT_FOUND 인지 확인
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    // userRepository.deleteById는 호출되지 않아야 함
    verify(userRepository, never()).deleteById(userId);
  }
}
