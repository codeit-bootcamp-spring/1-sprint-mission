package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user_status.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.UserStatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.Instant;
import java.util.List;

import static com.sprint.mission.discodeit.constant.FileConstant.*;
import static com.sprint.mission.discodeit.constant.FileConstant.BINARY_CONTENT_FILE;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class UserStatusServiceImplIntegrationTest {

  @Autowired
  private UserStatusService userStatusService;
  @Autowired
  private UserService userService;

  @Autowired private UserRepository userRepository;
  @Autowired private UserStatusRepository userStatusRepository;
  private User user;

  @BeforeEach
  void setUp() {

  userRepository.clear();
  userStatusRepository.clear();

    user = userService.createUser(
        new CreateUserDto(
            "username",
            "pwd",
            "test@example.com",
            "testNickname",
            "01012341234",
            new byte[]{1},
            "profileImage",
            "jpg",
            "description"
        )
    );
  }

/*
  @Test
  void UserStatus_생성_성공(){
    CreateUserStatusDto dto = new CreateUserStatusDto(user.getUUID(), Instant.now());
    UserStatus status = userStatusService.create(dto);

    assertThat(status).isNotNull();
    assertThat(status.getUserId()).isEqualTo(user.getUUID());
    assertThat(status.getUserStatus()).isEqualTo(UserStatusType.ONLINE);
  }
*/

  @Test
  void 중복된_생성_실패() {
    CreateUserStatusDto dto = new CreateUserStatusDto(user.getUUID(), Instant.now());

    assertThatThrownBy(() -> userStatusService.create(dto)).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void 존재하지_않는_사용자_UserStatus_생성_실패() {
    CreateUserStatusDto dto = new CreateUserStatusDto("invalid-id", Instant.now());
    assertThatThrownBy(() -> userStatusService.create(dto)).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void id_로_조회_성공() {
    UserStatus status = userStatusService.findByUserId(user.getUUID());
    UserStatus statusById = userStatusService.find(status.getUUID());

    assertThat(status).isEqualTo(statusById);
    assertThat(statusById.getUserId()).isEqualTo(user.getUUID());
  }

  @Test
  void 존재하지_않는_UserStatus_조회_실패() {
    assertThatThrownBy(() -> userStatusService.find("invalid")).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void 모든_UserStatus_조회_성공() {
    User tmpUser = userService.createUser(
        new CreateUserDto(
            "uname",
            "pwd2",
            "email2@email.com",
            "nick2",
            "01012312312",
            null,
            null,
            null,
            "description"
        )
    );

    List<UserStatus> statuses = userStatusService.findAll();

    assertThat(statuses).isNotEmpty();
    assertThat(statuses).hasSize(2);

  }

  @Test
  void UserStatus_업데이트_성공() {
    UserStatus status = userStatusService.findByUserId(user.getUUID());
    Instant timeBefore = status.getLastOnlineAt();

    UpdateUserStatusDto dto = new UpdateUserStatusDto(status.getUUID(), Instant.now());
    UserStatus updatedStatus = userStatusService.update(dto);

    assertThat(updatedStatus.getLastOnlineAt()).isAfter(timeBefore);
  }

  @Test
  void UserStatus_UserId_로_업데이트_성공() {
    UserStatus status = userStatusService.findByUserId(user.getUUID());
    Instant timeBefore = status.getLastOnlineAt();

    UpdateUserStatusDto dto = new UpdateUserStatusDto(status.getUUID(), Instant.now());
    UserStatus updatedStatus = userStatusService.updateByUserId(user.getUUID(), dto);

    assertThat(updatedStatus.getLastOnlineAt()).isAfter(timeBefore);
  }

  @Test
  void UserStatus_삭제() {
    UserStatus status = userStatusService.findByUserId(user.getUUID());
    userStatusService.delete(status.getUUID());

    assertThat(userStatusService.findAll()).hasSize(0);
  }
}
