package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateAuthTest {

    @InjectMocks
    ValidateAuth validateAuth;

    @Mock
    UserRepository userRepository;

    @Test
    void validateLogin_Success(){
        // given
        User user = new User("코드잇", "test@gmail.com", "01012345678", "qwer123$");

        // when
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // then
        assertDoesNotThrow(() -> validateAuth.validateLogin(user.getUsername(), user.getPassword()));

    }

    @Test
    void validateLogin_UserNotFound() {
        // given
        String username = "코드잇";
        String password = "1234";

        // when
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateAuth.validateLogin(username, password));
    }

    @Test
    void validateLogin_InvalidPassword(){
        // given
        User user = new User("코드잇", "test@gmail.com", "01012345678", "qwer123$");
        String invalidPassword = "1234";

        // when
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // then
        assertThrows(InvalidResourceException.class, () -> validateAuth.validateLogin(user.getUsername(), invalidPassword));
    }
}