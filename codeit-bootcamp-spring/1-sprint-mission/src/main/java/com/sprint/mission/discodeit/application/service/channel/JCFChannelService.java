package com.sprint.mission.discodeit.application.service.channel;

import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelNameRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelSubjectRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.CreateChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.DeleteChannelRequestDto;
import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public JCFChannelService(
            ChannelRepository channelRepository
    ) {
        this.channelRepository = channelRepository;
    }

    @Override
    public ChannelResponseDto create(CreateChannelRequestDto requestDto) {
        ChannelType channelType = ChannelType.valueOf(requestDto.channelType());    // TODO 예외 발생할 수 있음 -> 이넘타입에 존재하지 않을 경우
        Channel createChannel = new Channel(requestDto.name(), channelType);
        Channel savedChannel = channelRepository.save(createChannel);
        return ChannelResponseDto.from(savedChannel);
    }

    @Override
    public Channel findOneByIdOrThrow(UUID id) {
        return channelRepository.findOneById(id)
                .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Override
    public void changeSubject(ChangeChannelSubjectRequestDto requestDto) {
        Channel foundChannel = findOneByIdOrThrow(requestDto.channelId());
        foundChannel.updateSubject(requestDto.subject());
        channelRepository.save(foundChannel);
    }

    @Override
    public void changeChannelName(ChangeChannelNameRequestDto requestDto) {
        Channel foundChannel = findOneByIdOrThrow(requestDto.channelId());
        foundChannel.updateName(requestDto.channelName());
        channelRepository.save(foundChannel);
    }

    @Override
    public void deleteChannel(DeleteChannelRequestDto requestDto) {
        Channel foundChannel = findOneByIdOrThrow(requestDto.channelId());
        channelRepository.deleteById(foundChannel.getId());
    }
}
