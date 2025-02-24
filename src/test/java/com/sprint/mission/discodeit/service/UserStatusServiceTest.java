package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileStorage;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserStatusServiceTest {

    private UserStatusRepository userStatusRepository;
    private UserRepository userRepository;
    private UserStatusValidator userStatusValidator;
    private UserValidator userValidator;
    private UserStatusService userStatusService;
    private FileStorage fileStorage;

    @BeforeEach
    void setUp() {
//        jcfSetUp();
        fileSetUp();
        userStatusValidator = new UserStatusValidator(userStatusRepository);
        userValidator = new UserValidator(userRepository);
        userStatusService = new BasicUserStatusService(userStatusRepository, userStatusValidator, userValidator);
    }

    @AfterEach
    void clean() {
        if (fileStorage != null) {
            fileStorage.clearDataDirectory();
        }
    }

    private void jcfSetUp() {
        userStatusRepository = new JCFUserStatusRepository();
        userRepository = new JCFUserRepository();
    }

    private void fileSetUp() {
        fileStorage = new FileStorage();
        userStatusRepository = new FileUserStatusRepository(fileStorage);
        userRepository = new FileUserRepository(fileStorage);
    }

    private User createUser(int num) {
        User user = User.of("test" + num, "nickname" + num, "email" + num, "password" + num);
        return userRepository.saveUser(user);
    }

    private UserStatus createUserStatus(User user) {
        UserStatus userStatus = UserStatus.of(user);
        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Nested
    @DisplayName("UserStatus 생성 테스트")
    class CreateUserStatusTest {

        @Test
        @DisplayName("UserStatus 생성 성공")
        void success() {
            // given
            User user = createUser(0);

            // when
            UserStatus userStatus = userStatusService.createUserStatus(user.getId());

            // then
            assertNotNull(userStatusRepository.findUserStatusByUser(user));
            assertNotNull(userStatusRepository.findUserStatusById(userStatus.getId()));
        }
    }

    @Nested
    @DisplayName("UserStatus 조회 테스트")
    class FindUserStatusTest {

        @Test
        @DisplayName("UserStatus 단일 조회 성공")
        void success() {
            // given
            User user = createUser(0);
            UserStatus userStatus = createUserStatus(user);

            // when
            UserStatus foundedUserStatus = userStatusService.findUserStatusById(userStatus.getId());

            // then
            assertEquals(userStatus.getId(), foundedUserStatus.getId());
            assertEquals(user, foundedUserStatus.getUser());
            assertFalse(foundedUserStatus.isOnline());
        }
    }

    @Nested
    @DisplayName("UserStatus 목록 조회")
    class FindAllUserStatuses {

        @Test
        @DisplayName("UserStatus 목록 조회 성공")
        void success() {
            // given
            User user = createUser(0);
            User user1 = createUser(1);
            UserStatus userStatus = createUserStatus(user);
            UserStatus userStatus1 = createUserStatus(user1);

            // when
            List<UserStatus> userStatusList = userStatusService.findAllUserStatuses();

            // then
            assertEquals(2, userStatusList.size());
            assertTrue(userStatusList.contains(userStatus));
            assertTrue(userStatusList.contains(userStatus1));
        }
    }

    @Nested
    @DisplayName("UserStatus 수정 테스트")
    class UpdateUserStatusTest {

        @Test
        @DisplayName("UserStatus 수정 성공 by userStatusId")
        void successByUserStatusId() {
            // given
            User user = createUser(0);
            UserStatus userStatus = createUserStatus(user);

            UpdateUserStatusByIdRequest updateUserStatusByIdRequest =
                    new UpdateUserStatusByIdRequest(userStatus.getId(), true);

            // when
            UserStatus updatedUserStatus = userStatusService.updateUserStatusById(updateUserStatusByIdRequest);

            // then
            assertEquals(userStatus.getId(), updatedUserStatus.getId());
            assertTrue(updatedUserStatus.isOnline());
        }

        @Test
        @DisplayName("UserStatus 수정 성공 by userId")
        void successByUserId() {
            // given
            User user = createUser(0);
            UserStatus userStatus = createUserStatus(user);

            UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest =
                    new UpdateUserStatusByUserIdRequest(true);

            // when
            UserStatus updatedUserStatus = userStatusService.updateUserStatusByUserId(
                    user.getId(), updateUserStatusByUserIdRequest);

            // then
            assertEquals(userStatus.getId(), updatedUserStatus.getId());
            assertTrue(updatedUserStatus.isOnline());
            assertEquals(user, updatedUserStatus.getUser());
        }
    }

    @Nested
    @DisplayName("UserStatus 삭제 테스트")
    class DeleteUserStatusTest {

        @Test
        @DisplayName("UserStatus 삭제 성공")
        void success() {
            // given
            User user = createUser(0);
            UserStatus userStatus = createUserStatus(user);

            // when
            userStatusService.deleteUserStatus(userStatus.getId());

            // then
            assertNull(userStatusRepository.findUserStatusById(userStatus.getId()));
        }
    }
}