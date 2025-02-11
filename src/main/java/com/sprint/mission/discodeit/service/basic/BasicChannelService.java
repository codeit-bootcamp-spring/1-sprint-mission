package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicChannelService implements ChannelService {
    private static final Logger log = LoggerFactory.getLogger(BasicChannelService.class);
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public ChannelDto createChannel(ChannelDto channelDto) {
        log.info("채널을 생성합니다: {}", channelDto.getName());
        Channel channel = new Channel(
                channelDto.getCreatedAt(),
                channelDto.getType(),
                channelDto.getName(),
                channelDto.getDescription()
        );
        channelRepository.save(channel);
        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                channel.getCreatedAt(),
                channelDto.getMemberIds()
        );
    }

    @Override
    public ChannelDto findChannelById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 ID로 채널을 찾을 수 없습니다: " + channelId));
        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                channel.getCreatedAt(),
                null
        );
    }

    @Override
    public List<ChannelDto> findAllChannels() {
        return channelRepository.findAll().stream()
                .map(channel -> new ChannelDto(
                        channel.getId(),
                        channel.getName(),
                        channel.getDescription(),
                        channel.getType(),
                        channel.getCreatedAt(),
                        null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ChannelDto updateChannel(UUID channelId, ChannelDto channelDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 ID로 채널을 찾을 수 없습니다: " + channelId));
        channel.update(channelDto.getName(), channelDto.getDescription());
        channelRepository.save(channel);
        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                channel.getCreatedAt(),
                channelDto.getMemberIds()
        );
    }

    @Override
    public void deleteChannel(UUID channelId) {
        log.info("채널을 삭제합니다: {}", channelId);
        channelRepository.deleteById(channelId);
    }
}
