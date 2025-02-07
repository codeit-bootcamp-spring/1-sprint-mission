package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@RequiredArgsConstructor
public class MessageValidator {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public boolean validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new CustomException(ExceptionText.INVALID_MESSAGE_CONTENT);
        }
        return true;
    }

    public boolean validateSender(UUID uuid) {
        if (!userRepository.existsById(uuid)) {
            throw new CustomException(ExceptionText.USER_NOT_FOUND);
        }
        return true;
    }

    public boolean validateDestinationChannel(UUID uuid) {
        if (!channelRepository.existsById(uuid)) {
            throw new CustomException(ExceptionText.CHANNEL_NOT_FOUND);
        }
        return true;
    }

    public boolean validateMessage(UUID authorId,UUID channelId,String content) {
        boolean isContentValid = validateContent(content);
        boolean isSenderValid = validateSender(authorId);
        boolean isDestinationChannelValid = validateDestinationChannel(channelId);

        return isContentValid && isSenderValid && isDestinationChannelValid;
    }
}
