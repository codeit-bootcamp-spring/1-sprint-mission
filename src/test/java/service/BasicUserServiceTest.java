package service;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
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
import com.sprint.mission.discodeit.util.FileType;
import com.sprint.mission.discodeit.util.FileTypeProcessor;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import com.sprint.mission.discodeit.validator.EntityValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
  private CreateUserDto createUserDto;
  @BeforeEach
  void setUp(){



    user1 = new User.UserBuilder("user1", PasswordEncryptor.hashPassword("pwd1"), "email1@gmail.com", "01012341232").nickname("nickname1").build();
    user2 = new User.UserBuilder("user2", "pwd2", "email2@gmail.com", "01012341234").nickname("nickname2").build();
    user3 = new User.UserBuilder("user3", "pwd3", "email3@gmail.com", "01012341233").nickname("nickname3").build();
    createUserDto = new CreateUserDto(
        "newUser",
        "securePwd123",
        "newUser@gmail.com",
        "newNickname",
        "01098765432",
        new byte[]{1,2,3,4,5},
        "imageName",
        "jpg",
        "description"
    );

  }


  @Test
  void testCreateUser_Success(){

    User user = userService.createUser(createUserDto);

    assertThat(createUserDto.password()).isNotEqualTo(user.getPassword());
    assertThat(createUserDto.username()).isEqualTo(user.getUsername());
    assertThat(createUserDto.email()).isEqualTo(user.getEmail());

    verify(userRepository, times(1)).create(user);
    verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));

    verify(binaryContentRepository).save(argThat(binaryContent ->
              binaryContent.getFileName().equals(createUserDto.imageName()) &&
              binaryContent.getData().length == createUserDto.profileImage().length
        ));

    verify(userStatusRepository).save(argThat(userStatus ->
            userStatus.getUserId().equals(user.getUUID()) &&
            userStatus.getCreatedAt() != null
        ));
  }


  @Test
  void testCreateUser_Fail_Invalid_Email(){
    createUserDto = new CreateUserDto(
        "newUser",
        "securePwd123",
        "",
        "newNickname",
        "01098765432",
        new byte[]{1,2,3,4,5},
        "imageName",
        "jpg",
        "description"
    );

    assertThatThrownBy(() -> userService.createUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(ERROR_INVALID_EMAIL);
  }

  @Test
  void testCreateUser_Fail_Duplicate_Email(){
    createUserDto = new CreateUserDto(
        "newUser",
        "securePwd123",
        "email1@gmail.com",
        "newNickname",
        "01098765432",
        new byte[]{1,2,3,4,5},
        "imageName",
        "jpg",
        "description"
    );

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    assertThatThrownBy(()->userService.createUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(DUPLICATE_EMAIL);
  }

  @Test
  void testCreateUser_Fail_Invalid_PhoneNumber(){
    createUserDto = new CreateUserDto(
        "newUser",
        "securePwd123",
        "email1@gmail.com",
        "newNickname",
        "010987",
        new byte[]{1,2,3,4,5},
        "imageName",
        "jpg",
        "description"
    );
    assertThatThrownBy(() -> userService.createUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(ERROR_INVALID_PHONE);
  }

  @Test
  void testCreateUser_Fail_Duplicate_PhoneNumber() {
    createUserDto = new CreateUserDto(
        "newUser",
        "securePwd123",
        "email111@gmail.com",
        "newNickname",
        "01012341234",
        new byte[]{1, 2, 3, 4, 5},
        "imageName",
        "jpg",
        "description"
    );

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    assertThatThrownBy(() -> userService.createUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(DUPLICATE_PHONE);
  }


  @Test
  void testCreateUser_Fail_Invalid_Nickname(){
    createUserDto = new CreateUserDto(
        "newUser",
        "securePwd123",
        "email111@gmail.com",
        "",
        "01012341231",
        new byte[]{1, 2, 3, 4, 5},
        "imageName",
        "jpg",
        "description"
    );

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));
    assertThatThrownBy(() -> userService.createUser(createUserDto)).isInstanceOf(UserValidationException.class).hasMessageContaining(ERROR_USERNAME_LENGTH);
  }

  @Test
  void testFindUserById_Success(){
    UserStatus mockStatus = new UserStatus(user1.getUUID(), Instant.now());

    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);
    when(userStatusRepository.findByUserId(user1.getUUID())).thenReturn(Optional.of(mockStatus));

    UserResponseDto responseDto = userService.findUserById(user1.getUUID());

    assertThat(responseDto).isNotNull();
    assertThat(responseDto.userId()).isEqualTo(user1.getUUID());
    assertThat(responseDto.email()).isEqualTo(user1.getEmail());

    verify(validator, times(1)).findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class));
    verify(userStatusRepository, times(1)).findByUserId(user1.getUUID());

  }

  @Test
  void testFindUserById_Fail_UserNotFound(){
    when(validator.findOrThrow(eq(User.class), eq("invalid-id"), any(UserNotFoundException.class))).thenThrow(new UserNotFoundException());

    assertThatThrownBy(() -> userService.findUserById("invalid-id")).isInstanceOf(UserNotFoundException.class);

  }

  @Test
  void testFindUserById_CreateStatusIfNotExists(){
    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);
    when(userStatusService.create(any(CreateUserStatusDto.class))).thenReturn(new UserStatus(user1.getUUID(), Instant.now()));
    when(userStatusRepository.findByUserId(user1.getUUID())).thenReturn(Optional.empty());
    UserResponseDto response = userService.findUserById(user1.getUUID());

    assertThat(response).isNotNull();
    assertThat(response.userId()).isEqualTo(user1.getUUID());
    assertThat(response.userStatus()).isNotNull();

    verify(userStatusService, times(1)).create(any());
    verify(userStatusRepository, times(1)).findByUserId(user1.getUUID());
  }

  @Test
  void testFindAllUsers(){

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
  void testUpdateUser(){
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

    userService.updateUser(user1.getUUID(), updateDto, "pwd1");

    assertThat("updatedUser").isEqualTo(user1.getUsername());
    assertThat(PasswordEncryptor.checkPassword("newPassword123", user1.getPassword())).isTrue();

    verify(userRepository, times(1)).update(user1);
    verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
  }

  @Test
  void testUpdateUser_Fail_WrongPassword(){
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
  void testDeleteUser_Success(){
    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);
    userService.deleteUser(user1.getUUID(), "pwd1");

    verify(userRepository, times(1)).delete(user1.getUUID());
    verify(userStatusRepository, times(1)).deleteByUserId(user1.getUUID());
    verify(binaryContentRepository, times(1)).deleteByUserId(user1.getUUID());
  }

  @Test
  void testDeleteUser_Fail_WrongPassword(){
    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);

    assertThatThrownBy(() -> userService.deleteUser(user1.getUUID(), "wrong-password")).isInstanceOf(UserValidationException.class).hasMessageContaining(PASSWORD_MATCH_ERROR);

    verify(userRepository, times(0)).delete(user1.getUUID());
    verifyNoInteractions(userStatusRepository);
    verifyNoInteractions(binaryContentRepository);
  }

}
