package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

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
  @Autowired
  private UserMapper userMapper;
  private CreateUserRequest userDto;

  @Value("${app.file.user-file}")
  private String userFilePath;

  @BeforeEach
  void setUp(){
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());
    userRepository.clear();
    userStatusRepository.clear();
    binaryContentRepository.clear();
    userDto = new CreateUserRequest(
        "testUser", "password123", "test@example.com",
        "testNickname", "01012345678", mockFile,
        "Test Description"
    );
  }

  @Test
  void 유저_로그인_성공(){
    UserResponseDto user = userService.saveUser(userDto);

    UserResponseDto response = authService.login(user.username(), "password123");

    assertThat(response).isNotNull();
    assertThat(response.userId()).isEqualTo(user.userId());
    assertThat(response.email()).isEqualTo(user.email());
  }

  @Test
  void 존재하지_않는_유저_로그인_실패(){
    assertThatThrownBy(() -> authService.login("nonExistentUser", "password123")).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 잘못된_비밀번호_로그인_실패(){
    UserResponseDto user = userService.saveUser(userDto);

    assertThatThrownBy(() -> authService.login(user.username(), "wrongPwd")).isInstanceOf(UserValidationException.class);
  }
}
