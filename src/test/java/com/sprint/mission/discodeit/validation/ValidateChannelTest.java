package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateChannelTest {

    @InjectMocks
    ValidateChannel validateChannel;

    @Test
    void validatePublicChannel_Success() {
        // given
        String name = "디스코드";
        String description = "코드잇";

        // when
        // then
        assertDoesNotThrow(() -> validateChannel.validatePublicChannel(name, description));
    }

    @Test
    void validChannelName_InvalidChannelName() {
        // given
        // 101개 문자
        String name = "qwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgq";

        // when
        // then
        assertThrows(InvalidResourceException.class, () -> validateChannel.validChannelName(name));
    }

    @Test
    void validDescription_InvalidChannelDescription() {
        // given
        String description = "qwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgqwertasdfgq";

        // when
        // then
        assertThrows(InvalidResourceException.class, () -> validateChannel.validDescription(description));
    }

    @Test
    void validatePublicChannelType_PrivateChannelTypeCannotModified() {
        // given
        ChannelType channelType = ChannelType.PRIVATE;

        // when
        // then
        assertThrows(InvalidResourceException.class, () -> validateChannel.validatePublicChannelType(channelType));
    }
}