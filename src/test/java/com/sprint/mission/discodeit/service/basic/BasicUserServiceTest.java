package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateResponse;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@Rollback(false)
@Slf4j
class BasicUserServiceTest {

  @Autowired
  private UserService userService;

  MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.txt",
      "text/plain",
      "dadwqdczxcqwddqdwd".getBytes()
  );

  @Test
  @DisplayName("유저 생성")
  void create() throws IOException {
    User user = new User("userA", "test@mail.com",
        "1234124", new BinaryContent(file.getName(), file.getSize(), file.getContentType()), null);

    UserCreateRequest userCreateRequest = new UserCreateRequest("userA", "test@mail.com",
        "1234124");

    UserCreateResponse joinUser = userService.join(userCreateRequest, file);

    UserCreateResponse findUserDto = userService.findById(joinUser.id());

    assertThat(findUserDto.username()).isEqualTo(user.getUsername());
    assertThat(findUserDto.email()).isEqualTo(user.getEmail());
  }

  @Test
  @DisplayName("유저 삭제")
  void delete() {
    List<UserCreateResponse> users = userService.findAll();
    userService.delete(users.get(0).id());

    List<UserCreateResponse> all = userService.findAll();

    assertThat(all.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("모든 유저 조회")
  void findAll() {
    List<UserCreateResponse> all = userService.findAll();

    assertThat(all.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("유저 업데이트")
  void update() {
    List<UserCreateResponse> all = userService.findAll();
    UserUpdateResponse updatedUser = userService.update(all.get(0).id(),
        new UserUpdateRequest("newName", "newemail@new.com", "newpassword"),
        file);

    assertThat(updatedUser.username()).isEqualTo("newName");
    assertThat(updatedUser.email()).isEqualTo("newemail@new.com");
  }

  @Test
  @DisplayName("유저 상태 정보 업데이트")
  void updateStatus() throws InterruptedException {
    UUID userId = userService.findAll().get(0).id();
    Instant now = Instant.now();
    log.error("업데이트 전: {}", now);

    Thread.sleep(1000);

    UserStatusUpdateResponse status = userService.updateOnlineStatus(userId,
        new UserStatusUpdateRequest(Instant.now()));
    log.error("업데이트 후: {}", status.lastActiveAt());

    assertThat(Duration.between(now, status.lastActiveAt()).getSeconds())
        .isGreaterThanOrEqualTo(1L);
  }

  @BeforeAll
  void setupTestData() throws IOException {
    for (int i = 1; i <= 10; i++) {
      String username = "user" + i;
      String email = "user" + i + "@mail.com";

      UserCreateRequest request = UserCreateRequest.builder()
          .username(username)
          .email(email)
          .password("password123")
          .build();

      MockMultipartFile file = new MockMultipartFile(
          "file",
          "profile" + i + ".txt",
          "text/plain",
          ("dummy content for user " + i).getBytes()
      );

      userService.join(request, file);
    }
  }
}