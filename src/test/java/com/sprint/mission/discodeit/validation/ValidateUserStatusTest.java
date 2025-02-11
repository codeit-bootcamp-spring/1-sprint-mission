package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateUserStatusTest {

    @InjectMocks
    ValidateUserStatus validateUserStatus;

    @Mock
    UserRepository userRepository;

    @Mock
    UserStatusRepository userStatusRepository;

    @Test
    void validateUserStatus_Success() {
        // given
        User user = new User();
        UUID userId = user.getUserId();

        // when
        when(userStatusRepository.existsByUserId(userId)).thenReturn(false);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // then
        assertDoesNotThrow(() -> validateUserStatus.validateUserStatus(userId));
    }

    @Test
    void validateDuplicateUserId_UserStatusAlreadyExists() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        when(userStatusRepository.existsByUserId(userId)).thenReturn(true);

        // then
        assertThrows(DuplicateResourceException.class, () -> validateUserStatus.validateDuplicateUserId(userId));
    }

    @Test
    void validateUser_UserNotFound() {
        // given
        User user = new User();
        UUID userId = user.getUserId();

        // when
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateUserStatus.validateUser(userId));
    }
}