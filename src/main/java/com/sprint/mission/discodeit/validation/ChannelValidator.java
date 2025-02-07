package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class ChannelValidator {
    private final ChannelRepository channelRepository;

    public void isUniqueName(String name) {
        List<Channel> channels = channelRepository.findAll();
        // List<Channel>에서 이름이 중복되는지 확인
        if (channels.stream().anyMatch(channel -> channel.getName().equals(name))) {
            throw new CustomException(ExceptionText.NAME_DUPLICATE);
        }
    }

}
