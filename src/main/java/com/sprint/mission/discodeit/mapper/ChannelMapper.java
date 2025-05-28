package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;

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
                getOnline(readStatus.getUser())))
            .toList()
            : Collections.emptyList();
    }

    private Instant getLastMessageAt() {
        return messageRepository.findLastMessage()
            .map(Message::getCreatedAt)
            .orElse(null);
    }

    private boolean getOnline(User user) {
        boolean online = false;
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (Objects.equals(((UserDetails) principal).getUsername(), user.getName())) {
                online = true;
            }
        }

        return online;
    }
}
