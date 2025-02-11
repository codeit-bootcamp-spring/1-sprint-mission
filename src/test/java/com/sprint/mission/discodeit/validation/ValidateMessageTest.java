package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
class ValidateMessageTest {
    @InjectMocks
    ValidateMessage validateMessage;

    @Mock
    UserRepository userRepository;

    @Mock
    ChannelRepository channelRepository;

    @Test
    void validateMessage_Success() {
        // given
        String message = "안녕하세요.";
        User user = new User();
        UUID userId = user.getUserId();
        Channel channel = new Channel();
        UUID channelId = channel.getId();

        // when
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

        // then
        assertDoesNotThrow(() -> validateMessage.validateMessage(message, userId, channelId));
    }

    @Test
    void validateContent_InvalidMessage() {
        // given
        String message = "qwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgq";

        // when
        // then
        assertThrows(InvalidResourceException.class, () -> validateMessage.validateContent(message));
    }

    @Test
    void validateUser_UserNotFound() {
        // given
        User user = new User();

        // when
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateMessage.validateUser(user.getUserId()));
    }

    @Test
    void validateChannel_ChannelNotFound() {
        // given
        Channel channel = new Channel();

        // when
        when(channelRepository.findById(channel.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateMessage.validateChannel(channel.getId()));
    }
}