package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateUserTest {

    @InjectMocks
    ValidateUser validateUser;

    @Mock
    UserRepository userRepository;

    @Test
    void validateUser_Success() {
        // given
        User user = new User("코드잇", "test@gmail.com", "01012345678", "qwer123$");

        // when
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        // then
        assertDoesNotThrow(() -> validateUser.validateUser(user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getPassword()));
    }

    @Test
    void validateDuplicateUsername_UsernameAlreadyExists() {
        // given
        String username = "코드잇";

        // when
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // then
        assertThrows(DuplicateResourceException.class, () -> validateUser.validateDuplicateUsername(username));
    }

    @Test
    void validateDuplicateEmail_EmailAlreadyExists() {
        // given
        String email = "test@gmail.com";

        // when
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // then
        assertThrows(DuplicateResourceException.class, () -> validateUser.validateDuplicateEmail(email));
    }

    @Test
    void validateUsername_InvalidUsername() {
        // given
        String username = "qwertasdfgqwertasdfgqwertasdfgqwertasdfg";

        // when
        // then
        assertThrows(InvalidResourceException.class, () -> validateUser.validateUsername(username));
    }

    @Test
    void validateEmail_InvalidEmail() {
        // given
        String email = "testgmail.com";

        // when
        // then
        assertThrows(InvalidResourceException.class, () -> validateUser.validateEmail(email));
    }

    @Test
    void validatePhoneNumber_InvalidPhoneNumber() {
        // given
        String phoneNumber = "0101212";

        // when
        // then
        assertThrows(InvalidResourceException.class, () -> validateUser.validatePhoneNumber(phoneNumber));
    }

    @Test
    void validatePassword_InvalidPassword() {
        // given
        String password = "qwer1234";

        // when
        // then
        assertThrows(InvalidResourceException.class, () -> validateUser.validatePassword(password));
    }
}