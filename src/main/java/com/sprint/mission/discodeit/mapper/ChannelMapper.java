package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.common.util.LoginStatusChecker;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper userMapper;
    private final LoginStatusChecker loginStatusChecker;

    public Channel toEntity(String name, String description, ChannelType channelType) {
        return Channel.of(name, description, channelType);
    }

    public ChannelDto toChannelDto(Channel channel) {
        return new ChannelDto(channel.getId(), channel.getChannelType(),
            channel.getName(), channel.getDescription(), getChannelUserList(channel),
            getLastMessageAt());
    }

    private List<UserDto> getChannelUserList(Channel channel) {
        return channel.isPrivate()
            ? readStatusRepository.findAllReadStatusByChannel(channel).stream()
            .map(readStatus -> userMapper.toUserDto(readStatus.getUser(),
                loginStatusChecker.getOnline(readStatus.getUser())))
            .toList()
            : Collections.emptyList();
    }

    private Instant getLastMessageAt() {
        return messageRepository.findLastMessage()
            .map(Message::getCreatedAt)
            .orElse(null);
    }
}
