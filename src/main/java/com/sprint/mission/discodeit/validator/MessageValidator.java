package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageValidator {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    public void validateMessage(String content, UUID userId, UUID channelId){
        validateContent(content);
        validateUserId(userId);
        validateChannelId(channelId);
    }

    public void validateContent(String message){
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("입력 값은 null이거나 공백일 수 없습니다.");
        }
    }

    public void validateUserId(UUID userId){
        User findUser = userRepository.findOne(userId);
        Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 User 가 없습니다."));
    }

    public void validateChannelId(UUID channelId){
        Channel findChannel = channelRepository.findOne(channelId);
        Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NoSuchElementException("해당 Channel 이 없습니다."));
    }
}
