package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelJoinDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public ChannelDto create(ChannelDto channelDTO) {
        User creator = userRepository.findById(channelDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        ChannelType channelType;
        try {
            if (channelDTO.getType() == null || channelDTO.getType().isEmpty()) {
                channelType = ChannelType.PUBLIC;
            } else {
                channelType = ChannelType.valueOf(channelDTO.getType().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            channelType = ChannelType.PUBLIC;
        }

        Channel channel = new Channel(
                channelDTO.getName(),
                channelDTO.getDescription(),
                channelType);

        Channel saved = channelRepository.save(channel);
        return convertToDTO(saved);
    }

    private ChannelDto convertToDTO(Channel channel) {
        return ChannelDto.builder()
                .id(channel.getId())
                .name(channel.getName())
                .description(channel.getDescription())
                .type(channel.getType().toString())
                .build();
    }

    @Override
    public ChannelDto find(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        return convertToDTO(channel);
    }

    @Override
    public Map<User, Channel> join(ChannelJoinDto joinDTO) {

        Channel channel = channelRepository.findById(joinDTO.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel Not Found"));

        User user = userRepository.findById(joinDTO.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("User Not Found"));

        channelRepository.save(channel);

        return Map.of(user, channel);
    }

    @Override
    public ChannelDto update(UUID id, ChannelDto channelDTO) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        // 채널 타입 처리 - 타입이 제공되지 않은 경우 기존 타입 유지
        ChannelType channelType = channel.getType(); // 기본값으로 기존 타입 사용
        
        if (channelDTO.getType() != null && !channelDTO.getType().isEmpty()) {
            try {
                channelType = ChannelType.valueOf(channelDTO.getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 채널 타입입니다: " + channelDTO.getType());
            }
        }

        channel.update(
                channelDTO.getName(),
                channelDTO.getDescription(),
                channelType
        );

        Channel saved = channelRepository.save(channel);
        return convertToDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.deleteById(id);
    }


    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



}