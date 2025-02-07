package service;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.AuthServiceImpl;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusRepository userStatusRepository;

  @InjectMocks
  private AuthServiceImpl authService;

  private User mockUser1;
  private UserStatus mockUserStatus;

  @BeforeEach
  void setUp(){
    mockUser1 = new User.UserBuilder("username1", "hashed_pwd1", "email1@email.com", "01012345567")
        .nickname("nickname1").build();

    mockUserStatus = new UserStatus(mockUser1.getUUID(), Instant.now());
  }

  @Test
  void testLogin_Success() throws InterruptedException {
    when(userRepository.findByUsername(mockUser1.getUsername())).thenReturn(Optional.ofNullable(mockUser1));

    try(MockedStatic<PasswordEncryptor> passwordMock = mockStatic(PasswordEncryptor.class)){

          Instant timeBeforeLogin = mockUserStatus.getLastOnlineAt();
          TimeUnit.MILLISECONDS.sleep(5);

          passwordMock.when(() -> PasswordEncryptor.checkPassword("pwd1", "hashed_pwd1"))
              .thenReturn(true);

          when(userStatusRepository.findByUserId(mockUser1.getUUID())).thenReturn(Optional.of(mockUserStatus));

          UserResponseDto result = authService.login("username1", "pwd1");

          assertThat(result).isNotNull();
          assertThat("username1").isEqualTo(result.username());
          assertThat(result.lastOnlineAt()).isNotEqualTo(timeBeforeLogin);

          verify(userRepository, times(1)).findByUsername(mockUser1.getUsername());
          verify(userStatusRepository, times(1)).findByUserId(mockUser1.getUUID());
          verify(userStatusRepository, times(1)).save(mockUserStatus);
          passwordMock.verify(() -> PasswordEncryptor.checkPassword("pwd1", "hashed_pwd1"), times(1));
    }
  }

  @Test
  void testLogin_Fail_UserNotFound(){
    when(userRepository.findByUsername("wrongUsername")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authService.login("wrongUsername", "pwd1")).isInstanceOf(UserNotFoundException.class);

    verify(userRepository, times(1)).findByUsername("wrongUsername");
    verifyNoInteractions(userStatusRepository);
  }

  @Test
  void testLogin_Fail_InvalidPassword(){
    when(userRepository.findByUsername(mockUser1.getUsername())).thenReturn(Optional.ofNullable(mockUser1));

    try(MockedStatic<PasswordEncryptor> passwordMock = mockStatic(PasswordEncryptor.class)){
      passwordMock.when(() -> PasswordEncryptor.checkPassword("wrongPwd", "hashed_pwd1")).thenReturn(false);

      assertThatThrownBy(() -> authService.login(mockUser1.getUsername(), "wrongPwd")).isInstanceOf(UserValidationException.class);

      verify(userRepository, times(1)).findByUsername(mockUser1.getUsername());
      verifyNoInteractions(userStatusRepository);
    }
  }
}
