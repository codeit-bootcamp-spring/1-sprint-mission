package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelJoinDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Override
    public ChannelDto create(ChannelDto channelDTO) {
        User creator = userRepository.findById(channelDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자", "id", channelDTO.getUserId()));

        ChannelType channelType;
        try {
            if (channelDTO.getType() == null || channelDTO.getType().isEmpty()) {
                channelType = ChannelType.PUBLIC;
            } else {
                channelType = ChannelType.valueOf(channelDTO.getType().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            log.warn("유효하지 않은 채널 타입: {}, 기본값 PUBLIC으로 설정합니다.", channelDTO.getType());
            channelType = ChannelType.PUBLIC;
        }

        Channel channel = new Channel(
                channelDTO.getName(),
                channelDTO.getDescription(),
                channelType);

        Channel saved = channelRepository.save(channel);
        return channelMapper.toDto(saved);
    }

    @Override
    public ChannelDto find(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("채널", "id", id));

        return channelMapper.toDto(channel);
    }

    @Override
    public Map<User, Channel> join(ChannelJoinDto joinDTO) {
        Channel channel = channelRepository.findById(joinDTO.getChannelId())
                .orElseThrow(() -> new ResourceNotFoundException("채널", "id", joinDTO.getChannelId()));

        User user = userRepository.findById(joinDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자", "id", joinDTO.getUserId()));

        channelRepository.save(channel);

        return Map.of(user, channel);
    }

    @Override
    public ChannelDto update(UUID id, ChannelDto channelDTO) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("채널", "id", id));

        ChannelType channelType = channel.getType();
        
        if (channelDTO.getType() != null && !channelDTO.getType().isEmpty()) {
            try {
                channelType = ChannelType.valueOf(channelDTO.getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("유효하지 않은 채널 타입입니다: " + channelDTO.getType());
            }
        }

        channel.update(
                channelDTO.getName(),
                channelDTO.getDescription(),
                channelType
        );

        Channel saved = channelRepository.save(channel);
        return channelMapper.toDto(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new ResourceNotFoundException("채널", "id", id);
        }
        channelRepository.deleteById(id);
    }

    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .map(channelMapper::toDto)
                .collect(Collectors.toList());
    }
}