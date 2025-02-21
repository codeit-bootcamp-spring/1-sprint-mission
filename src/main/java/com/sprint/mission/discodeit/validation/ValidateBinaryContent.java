package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateBinaryContent {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public void validateBinaryContent(UUID userId, UUID messageId){
        validateUserId(userId);
        validateMessageId(messageId);
    }

    public void validateUserId(UUID userId){
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public void validateMessageId(UUID messageId){
        messageRepository.findByMessageId(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found."));
    }
}
