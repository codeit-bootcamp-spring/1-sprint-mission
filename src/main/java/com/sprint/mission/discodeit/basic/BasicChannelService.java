package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelJoinDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public Channel create(ChannelDTO channelDTO) {
        Channel channel = new Channel(
                channelDTO.getName(),
                channelDTO.getDescription(),
                ChannelType.valueOf(channelDTO.getType().toUpperCase())
        );
        return channelRepository.save(channel);
    }

    @Override
    public Map<User, Channel> join(ChannelJoinDTO joinDTO) {

        Channel channel = channelRepository.findById(joinDTO.getChannelName())
                .orElseThrow(() -> new IllegalArgumentException("Channel Not Found"));

        User user = userRepository.findById(joinDTO.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("User Not Found"));

        channel.addMember(user);
        channelRepository.save(channel);

        return Map.of(user, channel);
    }

    @Override
    public Channel update(String id, ChannelDTO channelDTO) {
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

        return channelRepository.save(channel);
    }

    @Override
    public void delete(String id) {
        channelRepository.deleteById(id);
    }

    @Override
    public Channel find(String id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }
}