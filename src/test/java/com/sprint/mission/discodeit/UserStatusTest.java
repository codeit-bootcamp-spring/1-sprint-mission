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

import java.nio.file.Path;

@SpringBootTest
public class UserStatusTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserStatusService userStatusService;

    private UserDTO testUser;

    @BeforeEach
    void before() {
        FileIOUtil.initializeFiles();
        CreateUserRequest request = new CreateUserRequest("테스트유저", "010-1234-1234", "Abcdefghi1234!", null);
    }

    @AfterEach
    void after() {
        FileIOUtil.convertSerToJson(Path.of("./result/users.ser"), Path.of("./json/users.json"), User.class);
        FileIOUtil.convertSerToJson(Path.of("./result/userstatus.ser"), Path.of("./json/userstatus.json"), UserStatus.class);
    }

    @DisplayName("사용자 생성하면 UserStatus가 같이 생성되는지 테스트")
    @Test
    void validUserStatus() {
        CreateUserRequest request = new CreateUserRequest("홍길동", "010-2189-9191", "Aldnweasdf1234!", null);
        UserDTO user = userService.create(request); // 유저 생성과 동시에 UserStatus가 생성된다.
        UserStatus findUserStatus = userStatusService.findByUserId(user.getId()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));

        Assertions.assertNotNull(findUserStatus);
    }


    @DisplayName("사용자 업데이트 후 UserStatus도 변경되는지 테스트")
    @Test
    void updateUserStatus() {
        UserStatus beforeUserstatus = userStatusService.findByUserId(testUser.getId()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        UpdateUserRequest request = new UpdateUserRequest(testUser.getId(), "TestPassword1234!!", null);
        User update = userService.update(request);
        UserStatus afterUserStatus = userStatusService.findByUserId(testUser.getId()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        Assertions.assertNotEquals(beforeUserstatus.getLastActiveAt(), afterUserStatus.getLastActiveAt());
    }
}
