package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final UserMapper userMapper;

    public ChannelDto toDto(Channel channel) {
        if (channel == null) {
            return null;
        }

        // 참여자 목록 수집
        List<UserDto> participants = new ArrayList<>();
        
        // ReadStatus를 통해 채널 참여자를 확인
        if (channel.getReadStatuses() != null) {
            participants = channel.getReadStatuses().stream()
                .map(ReadStatus::getUser)
                .distinct()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        }
        
        // 마지막 메시지 시간 찾기
        Instant lastMessageAt = null;
        if (channel.getMessages() != null && !channel.getMessages().isEmpty()) {
            // 가장 최근 메시지의 시간을 찾음
            lastMessageAt = channel.getMessages().stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
        }

        return ChannelDto.builder()
                .id(channel.getId())
                .name(channel.getName())
                .description(channel.getDescription())
                .type(channel.getType().toString())
                .participants(participants)
                .lastMessageAt(lastMessageAt)
                .build();
    }

    public Channel toEntity(ChannelDto dto) {
        if(dto == null) {
            return null;
        }

        return Channel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(ChannelType.valueOf(dto.getType()))
                .build();
    }
}
