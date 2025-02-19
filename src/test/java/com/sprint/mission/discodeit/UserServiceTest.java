package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDTO;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.FileIOUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserStatusService userStatusService;

    private UserDTO testUser;

    @BeforeEach
    void before() {
        FileIOUtil.initializeFiles();
        CreateUserRequest request = new CreateUserRequest("테스트유저", "010-1234-1234", "Abcdefghi1234!", null);
        testUser = userService.create(request);
    }

    @AfterEach
    void after() {
        FileIOUtil.initializeFiles();
    }

    @DisplayName("사용자 생성 테스트")
    @Test
    void createUser() {
        CreateUserRequest request = new CreateUserRequest("홍길동", "010-2189-9191", "Aldnweasdf1234!", null);
        UserDTO user = userService.create(request);
        Assertions.assertEquals("홍길동", user.getName());
    }

    @DisplayName("사용자 생성 시 전화번호 형식을 지켜야 한다.")
    @Test
    void validPhone() {
        CreateUserRequest request = new CreateUserRequest("홍길동", "010-1231233-9191", "Aldnweasdf1234!", null);

        ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> {
            userService.create(request);
        });

        Assertions.assertEquals(ErrorCode.INVALID_PHONE.getDescription(), exception.getMessage());
    }

    @DisplayName("사용자 생성 시 비밀번호 형식을 지켜야 한다.")
    @Test
    void validPassword() {
        CreateUserRequest request = new CreateUserRequest("홍길동", "010-1234-9191", "1234", null);

        ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> {
            userService.create(request);
        });

        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD.getDescription(), exception.getMessage());
    }


    @DisplayName("동일한 사용자의 이름이 존재하면 안 된다")
    @Test
    void duplicateUserName() {
        CreateUserRequest request1 = new CreateUserRequest("홍길동", "010-1234-9191", "Aldnweasdf1234!", null);
        CreateUserRequest request2 = new CreateUserRequest("홍길동", "010-3211-1211", "Aldnweasdf1234!", null);

        userService.create(request1);
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> {
            userService.create(request2);
        });

        Assertions.assertEquals(ErrorCode.DUPLICATE_NAME.getDescription(), exception.getMessage());
    }

    @DisplayName("동일한 핸드폰 번호가 존재하면 안 된다")
    @Test
    void duplicatePhone() {
        CreateUserRequest request1 = new CreateUserRequest("홍길동", "010-1234-1234", "Aldnweasdf1234!", null);
        CreateUserRequest request2 = new CreateUserRequest("김영희", "010-1234-1234", "Aldnweasdf1234!", null);

        userService.create(request1);
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> {
            userService.create(request2);
        });

        Assertions.assertEquals(ErrorCode.DUPLICATE_PHONE.getDescription(), exception.getMessage());
    }

    @DisplayName("회원 id로 해당하는 회원 찾기")
    @Test
    void findUser() {
        UserDTO findUser = userService.find(testUser.getId());

        Assertions.assertEquals(findUser.getName(), testUser.getName());
    }

    @DisplayName("회원 정보 업데이트 하기")
    @Test
    void updateUser() {
        UpdateUserRequest request = new UpdateUserRequest(testUser.getId(), "ChangePassword!!", null);
        User updateUser = userService.update(request);
        Assertions.assertEquals(updateUser.getPassword(), request.password());
    }

    @DisplayName("사용자를 삭제하면 User Status도 같이 삭제되어야 한다.")
    @Test
    void deleteUser() {
        userService.delete(testUser.getId());
        Optional<UserStatus> findUserStatus = userStatusService.findByUserId(testUser.getId());
        Assertions.assertEquals(findUserStatus, Optional.empty());
    }
}
