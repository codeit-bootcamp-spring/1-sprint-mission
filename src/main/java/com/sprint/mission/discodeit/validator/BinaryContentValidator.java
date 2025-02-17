package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BinaryContentValidator {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public void validateBinaryContent(UUID userId, UUID messageId){
        validateUserId(userId);
        validateMessageId(messageId);
    }

    public void validateUserId(UUID userId){
        if(userId !=null){
            User findUser = userRepository.findOne(userId);
            Optional.ofNullable(findUser)
                    .orElseThrow(() -> new NoSuchElementException("해당 User 가 없습니다."));
        }
    }

    public void validateMessageId(UUID messageId){
        if(messageId != null){
            Message findMessage = messageRepository.findOne(messageId);
            Optional.ofNullable(findMessage)
                    .orElseThrow(() -> new NoSuchElementException("해당 Message 가 없습니다."));
        }

    }
}
