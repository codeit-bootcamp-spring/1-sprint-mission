package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileUserServiceTest {

    private FileUserService fileUserService;
    private FileUserRepository fileUserRepository;
    private InputHandler inputHandler;
    private UUID testUserId;
    private final String USERS_PATH = "users/";

    @BeforeEach
    void setUp() {
        fileUserRepository = new FileUserRepository();
        inputHandler = mock(InputHandler.class);
        fileUserService = new FileUserService(fileUserRepository, inputHandler);
    }

    @Test
    @DisplayName("유저 생성 테스트")
    void testCreateUser(){
        UUID id = fileUserService.createUser("TestUserCreate");
        assertNotNull(fileUserService.getUserById(id));
    }

    @Test
    @DisplayName("모든 유저 보기 테스트")
    void testshowAllUsers(){
        fileUserService.createUser("TestUserCreate");
        assertEquals(1, fileUserService.showAllUsers());
    }
    @Test
    @DisplayName("특정 유저 보기 테스트")
    void testGetUserById(){
        UUID id = fileUserService.createUser("TestUserReadAll");
        assertNotNull(fileUserService.getUserById(id));
    }
    @Test
    @DisplayName("모든 유저 삭제 테스트")
    void testClearAllUsers(){
        fileUserService.createUser("testUserDeleteAll");
        fileUserService.clearAllUsers();
        assertEquals(0, fileUserService.showAllUsers());
    }
    @Test
    @DisplayName("특정 유저 삭제 테스트")
    void testRemoveUserById(){
        UUID id = fileUserService.createUser("testRemoveUserById");
        fileUserService.removeUserById(id);
        assertNull(fileUserService.getUserById(id));
    }


    @Test
    @DisplayName("유저 닉네임 업데이트 테스트")
    void testUpdateUserNickname() {
        User testUser = new User("OldNickname");
        fileUserRepository.saveUser(testUser);
        testUserId = testUser.getId();  // 생성된 유저의 ID를 저장

        // 새로운 닉네임 설정 모킹
        String newNickname = "NewNickname";
        when(inputHandler.getNewInput()).thenReturn(newNickname);

        // 닉네임 변경 실행
        fileUserService.updateUserNickname(testUserId);

        // 변경된 유저 불러오기 및 검증
        User updatedUser = fileUserService.getUserById(testUserId);
        assertNotNull(updatedUser, "User should exist");
        assertEquals(newNickname, updatedUser.getNickname(), "Nickname should be updated");
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 생성된 파일 삭제
        File testFile = new File(USERS_PATH + testUserId + ".ser");
        if (testFile.exists()) {
            assertTrue(testFile.delete(), "Test user file should be deleted");
        }
    }
}
