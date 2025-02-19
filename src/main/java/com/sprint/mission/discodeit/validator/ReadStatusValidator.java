package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadStatusValidator {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    public void validateReadStatus(UUID userId, UUID channelId){
        validateUserId(userId);
        validateChannelId(channelId);
        checkDuplicateReadStatus(userId, channelId);
    }

    public void validateUserId(UUID userId){
        User findUser = userRepository.findOne(userId);
        Optional.ofNullable(findUser)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateChannelId(UUID channelId){
        Channel findChannel = channelRepository.findOne(channelId);
        Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    private void checkDuplicateReadStatus(UUID userId, UUID channelId) {
        if (readStatusRepository.findByUserIdAndChannlId(userId, channelId).isPresent()) {
            throw new BadRequestException(ErrorCode.READ_STATUS_DUPLICATE);
        }
    }

}
