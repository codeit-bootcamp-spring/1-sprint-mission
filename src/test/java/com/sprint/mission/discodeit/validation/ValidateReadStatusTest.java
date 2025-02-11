package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
class ValidateReadStatusTest {
    @InjectMocks
    ValidateReadStatus validateReadStatus;

    @Mock
    ChannelRepository channelRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ReadStatusRepository readStatusRepository;

    @Test
    void validateReadStatus_Success() {
        // given
        Channel channel = new Channel();
        UUID channelId = channel.getId();
        User user = new User();
        UUID userId = user.getUserId();

        // when
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(readStatusRepository.existsByUserIdAndChannelId(userId, channelId)).thenReturn(false);

        // then
        assertDoesNotThrow(() -> validateReadStatus.validateReadStatus(channelId, userId));
    }

    @Test
    void validateChannel_ChannelNotFound() {
        // given
        Channel channel = new Channel();
        UUID channelId = channel.getId();

        // when
        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateReadStatus.validateChannel(channelId));
    }

    @Test
    void validateUser_UserNotFound() {
        // given
        User user = new User();
        UUID userId = user.getUserId();

        // when
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateReadStatus.validateUser(userId));
    }

    @Test
    void validateDuplicateReadStatus_ReadStatusAlreadyExists() {
        // given
        Channel channel = new Channel();
        UUID channelId = channel.getId();
        User user = new User();
        UUID userId = user.getUserId();

        // when
        when(readStatusRepository.existsByUserIdAndChannelId(userId, channelId)).thenReturn(true);

        // then
        assertThrows(DuplicateResourceException.class, () -> validateReadStatus.validateDuplicateReadStatus(channelId, userId));
    }

    @Test
    void testValidateReadStatus_ReadStatusNotFound() {
        // given
        ReadStatus readStatus = new ReadStatus();

        // when
        when(readStatusRepository.findById(readStatus.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> validateReadStatus.validateReadStatus(readStatus.getId()));
    }
}