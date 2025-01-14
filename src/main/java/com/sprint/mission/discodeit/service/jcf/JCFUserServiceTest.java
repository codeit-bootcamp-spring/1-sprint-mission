package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.impl.InMemoryUserRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // mock(), when() 메서드를 사용하기 위한 import



public class JCFUserServiceTest {
    @Test
    void testCreateUserChannel(){
        InputHandler mockInputHandler = mock(InputHandler.class);
        UserRepository userRepository = new InMemoryUserRepository();

        JCFUserService userService = new JCFUserService(userRepository, mockInputHandler);

        UUID id = userService.createUser("TestUserCreate");
        assertNotNull(userService.getUserById(id));
    }

    @Test
    void testGetAllUsers(){
        InputHandler mockInputHandler = mock(InputHandler.class);
        UserRepository userRepository = new InMemoryUserRepository();

        JCFUserService userService = new JCFUserService(userRepository, mockInputHandler);
        userService.createUser("TestUserReadAll1");
        userService.createUser("TestUserReadAll2");

        assertEquals(2, userService.showAllUsers());
    }

    @Test
    void testReadUser(){
        InputHandler mockInputHandler = mock(InputHandler.class);
        UserRepository userRepository = new InMemoryUserRepository();

        JCFUserService userService = new JCFUserService(userRepository, mockInputHandler);
        UUID id = userService.createUser("TestUserReadUser");

        assertNotNull(userService.getUserById(id));
    }

    @Test
    void testUpdateNickname(){
        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getNewInput()).thenReturn("겨울");

        UserRepository userRepository = new InMemoryUserRepository();

        JCFUserService userService = new JCFUserService(userRepository, mockInputHandler);
        UUID id = userService.createUser("TestUserUpdateNickname");

        userService.updateUserNickname(id);

        assertEquals("겨울", userService.getUserById(id));
    }

    @Test
    void testDeleteAll(){
        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");
        UserRepository userRepository = new InMemoryUserRepository();


        JCFUserService userService = new JCFUserService( userRepository, mockInputHandler);
        UUID userId1 = userService.createUser("testUserDeleteAll1");
        UUID userId2 = userService.createUser("testUserDeleteAll2");
        userService.clearAllUsers();

        assertNull(userService.getUserById(userId1));
        assertNull(userService.getUserById(userId2));
    }

    @Test
    void testDeleteUser(){
        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");
        UserRepository userRepository = new InMemoryUserRepository();

        JCFUserService userService = new JCFUserService(userRepository, mockInputHandler);
        UUID id = userService.createUser("testUserDeleteUser");
        userService.removeUserById(id);

        assertNull(userService.getUserById(id));
    }

}
