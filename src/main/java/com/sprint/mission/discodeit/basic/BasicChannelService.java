package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelJoinDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final RestClient.Builder builder;

    @Override
    public ChannelDto create(ChannelDto channelDTO) {
        User creator = userRepository.findById(channelDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Channel channel = new Channel(
                channelDTO.getName(),
                channelDTO.getDescription(),
                ChannelType.valueOf(channelDTO.getType().toUpperCase()));

        channel.addMember(creator);
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
    public ChannelDto find(String id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        return convertToDTO(channel);
    }

    @Override
    public Map<User, Channel> join(ChannelJoinDto joinDTO) {

        Channel channel = channelRepository.findById(joinDTO.getChannelName())
                .orElseThrow(() -> new IllegalArgumentException("Channel Not Found"));

        User user = userRepository.findById(joinDTO.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("User Not Found"));

        channel.addMember(user);
        channelRepository.save(channel);

        return Map.of(user, channel);
    }

    @Override
    public ChannelDto update(String id, ChannelDto channelDTO) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        String channelTypeStr = Optional.ofNullable(channelDTO.getType())
                .map(String::toUpperCase)
                .orElseThrow(() -> new IllegalArgumentException("Channel type cannot be null"));

        if (!EnumUtils.isValidEnum(ChannelType.class, channelTypeStr)) {
            throw new IllegalArgumentException("Invalid channel type: " + channelTypeStr);
        }

        ChannelType channelType = ChannelType.valueOf(channelTypeStr);
        channel.update(
                channelDTO.getName(),
                channelDTO.getDescription(),
                channelType
        );

        Channel saved = channelRepository.save(channel);
        return convertToDTO(saved);
    }

    @Override
    public void delete(String id) {
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