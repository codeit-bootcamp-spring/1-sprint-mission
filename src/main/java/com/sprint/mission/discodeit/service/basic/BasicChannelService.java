package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelValidator validator;

    private final ReadStatusService readStatusService;

    @Override
    public ChannelResponseDto create(ChannelCreateRequestDto channelCreateRequestDto) {
        if (channelCreateRequestDto.type() == ChannelType.PUBLIC) {
            return createPublicChannel(channelCreateRequestDto);
        }
        return createPrivateChannel(channelCreateRequestDto);
    }

    @Override
    public ChannelResponseDto createPublicChannel(ChannelCreateRequestDto channelCreateRequestDto) {
        validator.validate(channelCreateRequestDto.name(), channelCreateRequestDto.introduction());
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, channelCreateRequestDto.name(),channelCreateRequestDto.introduction()));
        return getChannelInfo(channel);
    }

    @Override
    public ChannelResponseDto createPrivateChannel(ChannelCreateRequestDto channelCreateRequestDto) {
        Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, channelCreateRequestDto.users()));
        channelCreateRequestDto.users().stream()
                .map(user -> readStatusService.create(channel.getId(), user));
        return getChannelInfo(channel);
    }

    @Override
    public ChannelResponseDto find(UUID channelId) {
        // TODO: 메시지 정보 포함
        Channel channel = Optional.ofNullable(channelRepository.find(channelId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));
        return getChannelInfo(channel);
    }

    @Override
    public List<ChannelResponseDto> findAll() {
        // TODO: 메시지 정보 포함
        List<Channel> channels = channelRepository.findAll();

        return channels.stream()
                .map(this::getChannelInfo)
                .toList();
    }

    @Override
    public ChannelResponseDto getChannelInfo(Channel channel) {
        return ChannelResponseDto.from(channel.getId(), channel.getType(),
                channel.getName(), channel.getIntroduction(), channel.getParticipants());
    }

    @Override
    public void update(UUID channelId, String name, String introduction) {
        validator.validate(name, introduction);
        Channel channel = find(channelId);
        channelRepository.update(channel, name, introduction);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = find(channelId);
        channelRepository.delete(channel);
    }
}
