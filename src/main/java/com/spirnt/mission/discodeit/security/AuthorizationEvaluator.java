package com.spirnt.mission.discodeit.security;

import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*
 * @PreAuthorize에서 복잡한 표현식을 관리하기 위한 컴포넌트
 */
@Component
@RequiredArgsConstructor
public class AuthorizationEvaluator {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    public boolean isMessageAuthor(UUID messageId, UUID userId) {
        return messageRepository.findById(messageId)
            .map(message -> message.getAuthor().getId().equals(userId))
            .orElse(false);
    }

    public boolean isReadStatusOwner(UUID readStatusId, UUID userId) {
        return readStatusRepository.findById(readStatusId)
            .map(readStatus -> readStatus.getUser().getId().equals(userId))
            .orElse(false);
    }

}
