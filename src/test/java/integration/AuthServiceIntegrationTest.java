package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class AuthServiceIntegrationTest {

  @Autowired
  private AuthService authService;

  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;
  private CreateUserDto userDto;

  @Value("${app.file.user-file}")
  private String userFilePath;

  @BeforeEach
  void setUp(){
    userRepository.clear();
    userStatusRepository.clear();
    binaryContentRepository.clear();
    userDto = new CreateUserDto(
        "testUser", "password123", "test@example.com",
        "testNickname", "01012345678", new byte[]{1},
        "profileImage", "jpg", "Test Description"
    );
  }

  @Test
  void 유저_로그인_성공(){
    User user = userService.createUser(userDto);

    UserResponseDto response = authService.login(user.getUsername(), "password123");

    assertThat(response).isNotNull();
    assertThat(response.userId()).isEqualTo(user.getUUID());
    assertThat(response.email()).isEqualTo(user.getEmail());
  }

  @Test
  void 존재하지_않는_유저_로그인_실패(){
    assertThatThrownBy(() -> authService.login("nonExistentUser", "password123")).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 잘못된_비밀번호_로그인_실패(){
    User user = userService.createUser(userDto);

    assertThatThrownBy(() -> authService.login(user.getUsername(), "wrongPwd")).isInstanceOf(UserValidationException.class);
  }
}
