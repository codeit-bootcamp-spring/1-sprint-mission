package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateMessage {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private static final String MESSAGE_REGEX = "^(?!\\s*$).{1,100}$";

    public void validateMessage(String message, UUID userId, UUID channelId){
        validateContent(message);
        validateUser(userId);
        validateChannel(channelId);
    }

    public void validateContent(String message) {
        if (message == null || !message.matches(MESSAGE_REGEX)) {
            throw new InvalidResourceException("Invalid message.");
        }
    }

    public void validateUser(UUID userId){
        if (userRepository.findByUserId(userId).isEmpty()){
            throw new ResourceNotFoundException("User not found.");
        }
    }

    public void validateChannel(UUID channelId){
        if (channelRepository.findById(channelId).isEmpty()){
            throw new ResourceNotFoundException("Channel not found.");
        }
    }

}
