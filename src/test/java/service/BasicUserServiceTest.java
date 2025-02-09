package service;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.util.FileType;
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
  private EntityValidator validator;
  @InjectMocks
  private BasicUserService userService;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  void setUp(){
    user1 = new User.UserBuilder("user1", "pwd1", "email1@gmail.com", "01012341232").nickname("nickname1").build();
    user2 = new User.UserBuilder("user2", "pwd2", "email2@gmail.com", "01012341234").nickname("nickname2").build();
    user3 = new User.UserBuilder("user3", "pwd3", "email3@gmail.com", "01012341233").nickname("nickname3").build();
  }

  @Test
  void testCreateUser(){
    CreateUserDto createUserDto = new CreateUserDto(
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

    User expectedUser = new User.UserBuilder(
        createUserDto.username(),
        createUserDto.password(),
        createUserDto.email(),
        createUserDto.phoneNumber())
        .nickname(createUserDto.nickname())
        .description(createUserDto.description())
        .build();

    UserStatus expectedStatus = new UserStatus(expectedUser.getUUID(), Instant.now());

    when(userRepository.findAll()).thenReturn(Collections.emptyList());

    when(userRepository.create(any(User.class))).thenReturn(expectedUser);
    when(userStatusRepository.save(any(UserStatus.class))).thenReturn(expectedStatus);

    User createUser = userService.createUser(createUserDto);

    assertThat(createUser).isNotNull();
    assertThat(createUserDto.username()).isEqualTo(createUser.getUsername());
    assertThat(createUserDto.email()).isEqualTo(createUser.getEmail());

    verify(userRepository, times(1)).create(any(User.class));
    verify(userStatusRepository, times(1)).save(any(UserStatus.class ));
    verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
  }
}
