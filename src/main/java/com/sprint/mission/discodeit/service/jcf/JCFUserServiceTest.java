package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.io.InputHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//import org.mockito.Mock;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*; // mock(), when() 메서드를 사용하기 위한 import



public class JCFUserServiceTest {
    @Test
    void testCreateUser(){
        // 너무... 불필요한 요소가 많이 생기는 것 아닌가
        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFUserService userService = new JCFUserService(mockInputHandler);
        userService.Create("TestUserCreate");
        assertNotNull(userService.getUser("TestUserCreate"));
    }

    @Test
    void testReadAllUsers(){
        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFUserService userService = new JCFUserService(mockInputHandler);
        userService.Create("TestUserReadAll1");
        userService.Create("TestUserReadAll2");
        assertEquals(2, userService.ReadAll());
    }

    @Test
    void testReadUser(){
        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFUserService userService = new JCFUserService(mockInputHandler);
        userService.Create("TestUserReadUser");
        assertNotNull(userService.ReadUser("TestUserReadUser"));
    }

    @Test
    void testUpdateNickname(){
        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getNewNickNameInput()).thenReturn("겨울");

        JCFUserService userService = new JCFUserService(mockInputHandler);
        userService.Create("TestUserUpdateNickname");
        userService.UpdateNickname("TestUserUpdateNickname");


        assertEquals("겨울", userService.getUser("겨울").getNickname());
    }

    @Test
    void testDeleteAll(){
        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");


        JCFUserService userService = new JCFUserService(mockInputHandler);
        userService.Create("testUserDeleteAll1");
        userService.Create("testUserDeleteAll2");
        userService.DeleteAll();
        assertNull(userService.getUser("testUserDeleteAll1"));
        assertNull(userService.getUser("testUserDeleteAll2"));
    }


    @Test
    void testDeleteUser(){
        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");

        JCFUserService userService = new JCFUserService(mockInputHandler);
        userService.Create("testUserDeleteUser");
        userService.DeleteUser("testUserDeleteUser");

        assertNull(userService.getUser("testUserDeleteUser"));
    }

}
