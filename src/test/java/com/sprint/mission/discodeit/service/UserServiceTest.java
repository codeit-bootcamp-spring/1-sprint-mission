package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @InjectMocks
    private BasicUserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "test@email.com", "password");
    }

    @Test
    void testCreateUser() {
        UserDTO userDTO = new UserDTO("newUser", "new@email.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(userDTO, null);

        assertNotNull(createdUser);
        assertNotEquals("testUser", createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDTO foundUser = userService.find(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(user.getId());

        assertDoesNotThrow(() -> userService.delete(user.getId()));
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}