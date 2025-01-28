package com.sprint.mission.discodeit.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


class FileUserServiceTest {
    private static final String TEST_FILE_PATH = "data/test_users.dat";
    private UserService userService;

    @BeforeEach
    void setUp() {
        // 기존 파일 삭제 및 새로운 파일 생성
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            assertTrue(file.delete(), "테스트 파일 삭제 실패");
        }
        userService = new FileUserService(TEST_FILE_PATH);
    }

    @Test
    @DisplayName("유저를 생성한다.")
    void testCreateUser() {
        // given
        User user = userService.createUser("testUser");

        // when

        // then
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());

        Map<UUID, User> users = userService.getUsers();
        assertEquals(1, users.size());
        assertTrue(users.containsKey(user.getId()));
    }

    @Test
    @DisplayName("유저를 생성하고 단일 조회를 한다")
    void testGetUser() {
        // given
        User user = userService.createUser("testUser");

        // when
        Optional<User> foundUser = userService.getUser(user.getId());

        // then
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("유저를 생성하고 업데이트한 후 조회한다")
    void testUpdateUser() {
        // given
        User user = userService.createUser("testUser");

        // when
        Optional<User> updatedUser = userService.updateUser(user.getId(), "updatedUser");

        // then
        assertTrue(updatedUser.isPresent());
        assertEquals("updatedUser", updatedUser.get().getUsername());

        Optional<User> foundUser = userService.getUser(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("updatedUser", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("유저를 생성하고 삭제한후 확인한다")
    void testDeleteUser() {
        // given
        User user = userService.createUser("testUser");

        // when
        Optional<User> deletedUser = userService.deleteUser(user.getId());

        // then
        assertTrue(deletedUser.isPresent());
        assertEquals("testUser", deletedUser.get().getUsername());

        Optional<User> foundUser = userService.getUser(user.getId());
        assertFalse(foundUser.isPresent());

        Map<UUID, User> users = userService.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("유저를 생성하고 새로운 서비스를 만들때 로드가 되는지 확인한다")
    void testPersistenceAcrossInstances() {
        // given
        User user = userService.createUser("persistentUser");

        // when
        UserService newServiceInstance = new FileUserService(TEST_FILE_PATH);

        // then
        Optional<User> foundUser = newServiceInstance.getUser(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("persistentUser", foundUser.get().getUsername());
    }
}
