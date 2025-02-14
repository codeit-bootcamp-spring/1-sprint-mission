package service;

import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user_status.CreateUserStatusDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import com.sprint.mission.discodeit.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.sprint.mission.discodeit.constant.UserConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private UserStatusService userStatusService;
  @Mock
  private BinaryContentService binaryContentService;
  @Mock
  private EntityValidator validator;
  @InjectMocks
  private BasicUserService userService;

  private User user1;
  private User user2;
  private User user3;
  private CreateUserRequest createUserDto;

  @BeforeEach
  void setUp() {
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());


    user1 = new User.UserBuilder("user1", PasswordEncryptor.hashPassword("pwd1"), "email1@gmail.com", "01012341232").nickname("nickname1").build();
    user2 = new User.UserBuilder("user2", "pwd2", "email2@gmail.com", "01012341234").nickname("nickname2").build();
    user3 = new User.UserBuilder("user3", "pwd3", "email3@gmail.com", "01012341233").nickname("nickname3").build();
    createUserDto = new CreateUserRequest(
        "newUser",
        "securePwd123",
        "newUser@gmail.com",
        "newNickname",
        "01098765432",
        mockFile,
        "description"
    );

  }


  @Test
  void testCreateUser_Success() {
    when(binaryContentService.create(any(CreateBinaryContentDto.class))).thenAnswer(
        invocation -> {
          CreateBinaryContentDto dto = invocation.getArgument(0);
          return new BinaryContent.BinaryContentBuilder(
              dto.userId(),
              dto.fileName(),
              dto.fileType(),
              dto.fileSize(),
              dto.data()
          ).isProfilePicture().build();
        }
    );

    when(userStatusService.create(any(CreateUserStatusDto.class))).thenAnswer(invocation -> {
      CreateUserStatusDto dto = invocation.getArgument(0);
      return new UserStatus(dto.userId(), dto.lastOnlineAt());
    });

    UserResponseDto user = userService.saveUser(createUserDto);

    assertThat(createUserDto.username()).isEqualTo(user.username());
    assertThat(createUserDto.email()).isEqualTo(user.email());

    verify(userRepository, times(1)).create(any(User.class));
    verify(userStatusService, times(1)).create(any(CreateUserStatusDto.class));

    verify(userRepository).create(argThat(savedUser ->
        savedUser.getUsername().equals(createUserDto.username()) &&
            savedUser.getEmail().equals(createUserDto.email()) &&
            PasswordEncryptor.checkPassword(createUserDto.password(), savedUser.getPassword())
    ));


    verify(userStatusService).create(argThat(userStatus ->
        userStatus.userId().equals(user.userId()) &&
            userStatus.lastOnlineAt() != null
    ));
  }


  @Test
  void testCreateUser_Fail_Invalid_Email() {
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());

    createUserDto = new CreateUserRequest(
        "newUser",
        "securePwd123",
        "",
        "newNickname",
        "01098765432",
        mockFile,
        "description"
    );

    assertThatThrownBy(() -> userService.saveUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(ERROR_INVALID_EMAIL);
  }

  @Test
  void testCreateUser_Fail_Duplicate_Email() {
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());

    createUserDto = new CreateUserRequest(
        "newUser",
        "securePwd123",
        "email1@gmail.com",
        "newNickname",
        "01098765432",
        mockFile,
        "description"
    );

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    assertThatThrownBy(() -> userService.saveUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(DUPLICATE_EMAIL);
  }

  @Test
  void testCreateUser_Fail_Invalid_PhoneNumber() {
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());

    createUserDto = new CreateUserRequest(
        "newUser",
        "securePwd123",
        "email1@gmail.com",
        "newNickname",
        "010987",
        mockFile,
        "description"
    );
    assertThatThrownBy(() -> userService.saveUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(ERROR_INVALID_PHONE);
  }

  @Test
  void testCreateUser_Fail_Duplicate_PhoneNumber() {
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());

    createUserDto = new CreateUserRequest(
        "newUser",
        "securePwd123",
        "email111@gmail.com",
        "newNickname",
        "01012341234",
        mockFile,
        "description"
    );

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    assertThatThrownBy(() -> userService.saveUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(DUPLICATE_PHONE);
  }


  @Test
  void testCreateUser_Fail_Invalid_Nickname() {
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());

    createUserDto = new CreateUserRequest(
        "newUser",
        "securePwd123",
        "email111@gmail.com",
        "",
        "01012341231",
        mockFile,
        "description"
    );

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));
    assertThatThrownBy(() -> userService.saveUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(ERROR_USERNAME_LENGTH);
  }

  @Test
  void testFindUserById_Success() {
    UserStatus mockStatus = new UserStatus(user1.getUUID(), Instant.now());

    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);
    when(userStatusService.findByUserId(user1.getUUID())).thenReturn(new UserStatus(user1.getUUID(), Instant.now()));

    UserResponseDto responseDto = userService.findUserById(user1.getUUID());

    assertThat(responseDto).isNotNull();
    assertThat(responseDto.userId()).isEqualTo(user1.getUUID());
    assertThat(responseDto.email()).isEqualTo(user1.getEmail());

    verify(validator, times(1)).findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class));
    verify(userStatusService, times(1)).findByUserId(user1.getUUID());

  }

  @Test
  void testFindUserById_Fail_UserNotFound() {
    when(validator.findOrThrow(eq(User.class), eq("invalid-id"), any(UserNotFoundException.class))).thenThrow(new UserNotFoundException());

    assertThatThrownBy(() -> userService.findUserById("invalid-id")).isInstanceOf(UserNotFoundException.class);

  }

  @Test
  void testFindUserById_CreateStatusIfNotExists() {
    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);
    when(userStatusService.findByUserId(user1.getUUID())).thenReturn(new UserStatus(user1.getUUID(), Instant.now()));

    UserResponseDto response = userService.findUserById(user1.getUUID());

    assertThat(response).isNotNull();
    assertThat(response.userId()).isEqualTo(user1.getUUID());
    assertThat(response.userStatus()).isNotNull();

    verify(userStatusService, times(1)).findByUserId(any());
  }

  @Test
  void testFindAllUsers() {

    UserStatus user1Status = new UserStatus(user1.getUUID(), Instant.now());
    when(userRepository.findAll()).thenReturn(List.of(user1, user2));
    when(userStatusService.mapUserToUserStatus(anySet())).thenReturn(Map.of(
        user1.getUUID(), user1Status,
        user2.getUUID(), new UserStatus(user2.getUUID(), Instant.now())
    ));


    List<UserResponseDto> responseList = userService.findAllUsers();

    assertThat(responseList).isNotNull();
    assertThat(responseList).hasSize(2);

    UserResponseDto res1 = responseList.get(0);
    UserResponseDto res2 = responseList.get(1);

    assertThat(res1.userId()).isEqualTo(user1.getUUID());
    assertThat(res1.username()).isEqualTo(user1.getUsername());

    assertThat(res2.userId()).isEqualTo(user2.getUUID());

    verify(userRepository, times(1)).findAll();
    verify(userStatusService, times(1)).mapUserToUserStatus(anySet());
    verify(binaryContentService, times(1)).mapUserToBinaryContent(anySet());
  }

  @Test
  void testUpdateUser() {

    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);

    when(binaryContentService.create(any(CreateBinaryContentDto.class))).thenAnswer(invocation -> {
      CreateBinaryContentDto dto = invocation.getArgument(0);
      return new BinaryContent.BinaryContentBuilder(
          dto.userId(),
          dto.fileName(),
          dto.fileType(),
          dto.fileSize(),
          dto.data()
      ).isProfilePicture().build();
    });

    UserUpdateDto updateDto = new UserUpdateDto(
        "updatedUser",
        "newPassword123",
        "updated@example.com",
        "updatedNickname",
        "01087654321",
        "updatedDescription",
        new byte[]{4, 5, 6},
        "newImage",
        "jpg"
    );

    userService.updateUser(user1.getUUID(), updateDto, "pwd1");

    assertThat("updatedUser").isEqualTo(user1.getUsername());
    assertThat(PasswordEncryptor.checkPassword("newPassword123", user1.getPassword())).isTrue();

    verify(userRepository, times(2)).update(user1);
    verify(binaryContentService, times(1)).create(any(CreateBinaryContentDto.class));
  }

  @Test
  void testUpdateUser_Fail_WrongPassword() {
    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);

    UserUpdateDto updateDto = new UserUpdateDto(
        "updatedUser",
        "newPassword123",
        "updated@example.com",
        "updatedNickname",
        "01087654321",
        "updatedDescription",
        new byte[]{4, 5, 6},
        "newImage",
        "jpg"
    );

    assertThatThrownBy(() -> userService.updateUser(user1.getUUID(), updateDto, "wrong-password")).isInstanceOf(UserValidationException.class).hasMessageContaining(PASSWORD_MATCH_ERROR);
  }

  @Test
  void testDeleteUser_Success() {
    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);

    userService.deleteUser(user1.getUUID(), "pwd1");

    verify(userRepository, times(1)).delete(user1.getUUID());
    verify(userStatusService, times(1)).deleteByUserId(user1.getUUID());
  }

  @Test
  void testDeleteUser_Fail_WrongPassword() {
    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);

    assertThatThrownBy(() -> userService.deleteUser(user1.getUUID(), "wrong-password")).isInstanceOf(UserValidationException.class).hasMessageContaining(PASSWORD_MATCH_ERROR);

    verify(userRepository, times(0)).delete(user1.getUUID());
    verifyNoInteractions(userStatusRepository);
    verifyNoInteractions(binaryContentRepository);
  }

}
