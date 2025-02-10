package com.sprint.mission.discodeit.application.service.channel;

import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelNameRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelSubjectRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.CreateChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.DeleteChannelRequestDto;
import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    public JCFChannelService(
            ChannelRepository channelRepository,
            UserService userService
    ) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public ChannelResponseDto create(UUID userId, CreateChannelRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        // TODO 예외 발생할 수 있음 -> 이넘타입에 존재하지 않을 경우
        ChannelType channelType = ChannelType.valueOf(requestDto.channelType());
        Channel createChannel = new Channel(requestDto.name(), channelType, foundUser);
        Channel savedChannel = channelRepository.save(createChannel);
        return ChannelResponseDto.from(savedChannel);
    }

    @Override
    public Channel findOneByIdOrThrow(UUID id) {
        return channelRepository.findOneById(id)
                .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Override
    public void changeSubject(UUID userId, ChangeChannelSubjectRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        Channel foundChannel = findOneByIdOrThrow(requestDto.channelId());
        throwIsNotManager(foundUser, foundChannel);
        foundChannel.updateSubject(requestDto.subject());
        channelRepository.save(foundChannel);
    }

    @Override
    public void changeChannelName(UUID userId, ChangeChannelNameRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        Channel foundChannel = findOneByIdOrThrow(requestDto.channelId());
        throwIsNotManager(foundUser, foundChannel);
        foundChannel.updateName(requestDto.channelName());
        channelRepository.save(foundChannel);
    }

    @Override
    public void deleteChannel(UUID userId, DeleteChannelRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        Channel foundChannel = findOneByIdOrThrow(requestDto.channelId());
        throwIsNotManager(foundUser, foundChannel);
        channelRepository.deleteById(foundChannel.getId());
    }

    private void throwIsNotManager(User foundUser, Channel foundChannel) {
        if (!foundChannel.isManager(foundUser)) {
            throw new IllegalArgumentException();
        }
    }
}
