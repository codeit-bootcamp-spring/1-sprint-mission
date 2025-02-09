package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ValidateChannel {

    private static final String CHANNEL_NAME = "^(?!\\s*$).{1,100}$";
    private static final String DESCRIPTION = "^(?!\\s*$).{1,100}$";

    public void validatePublicChannel(String name, String description, ChannelType channelType){
        validChannelName(name);
        validDescription(description);
        validChannelType(channelType);
    }

    public void validChannelName(String name) {
        if (name == null || !name.matches(CHANNEL_NAME)) {
            throw new InvalidResourceException("Invalid channel name.");
        }
    }

    public void validDescription(String description) {
        if (description == null || !description.matches(DESCRIPTION)) {
            throw new InvalidResourceException("Invalid channel description.");
        }
    }

    public void validChannelType(ChannelType channelType) {
        if (channelType == null || !Arrays.asList(ChannelType.values()).contains(channelType)) {
            throw new InvalidResourceException("Invalid channel type.");
        }
    }

    public void validatePublicChannelType(ChannelType channelType){
        if (channelType.equals(ChannelType.PRIVATE)){
            throw new InvalidResourceException("Private channel Type cannot modified.");
        }
    }
}
