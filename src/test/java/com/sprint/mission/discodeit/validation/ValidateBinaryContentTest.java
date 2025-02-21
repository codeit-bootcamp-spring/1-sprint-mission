package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
class ValidateBinaryContentTest {

    @InjectMocks
    ValidateBinaryContent validateBinaryContent;

    @Mock
    UserRepository userRepository;

    @Mock
    MessageRepository messageRepository;

    @Test
    void validateUserId_Success(){
        // given
        User user = new User("코드잇", "test@gmail.com", "01012345678", "qwer123$");
        UUID userId = user.getUserId();
        Message message = new Message("안녕하세요.", userId, UUID.randomUUID(), null);
        UUID messageId = message.getId();

        // when
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(messageRepository.findByMessageId(messageId)).thenReturn(Optional.of(message));

        // then
        assertDoesNotThrow(() -> validateBinaryContent.validateBinaryContent(userId, messageId));
    }

    @Test
    void validateUserId_UserNotFound() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateBinaryContent.validateUserId(userId));
    }

    @Test
    void validateMessageId_MessageNotFound() {
        // given
        UUID messageId = UUID.randomUUID();

        // when
        when(messageRepository.findByMessageId(messageId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateBinaryContent.validateMessageId(messageId));
    }
}