package com.sprint.mission.discodeit.application.service.channel;

import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelNameRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelSubjectRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.CreateChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.DeleteChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.InviteChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.FoundChannelResponseDto;
import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.MessageService;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.enums.ChannelVisibility;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.readStatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import com.sprint.mission.discodeit.repository.readstatus.interfaces.ReadStatusRepository;
import java.time.LocalDateTime;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private final ReadStatusRepository readStatusRepository;
    private final MessageService messageService;

    public JCFChannelService(
            ChannelRepository channelRepository,
            UserService userService,
            ReadStatusRepository readStatusService,
            MessageService messageService
    ) {
        this.channelRepository = channelRepository;
        this.userService = userService;
        this.readStatusRepository = readStatusService;
        this.messageService = messageService;
    }

    @Override
    public ChannelCreateResponseDto createPublicChannel(UUID userId, CreateChannelRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        ChannelType channelType = requestDto.channelType();
        Channel createChannel = new Channel(requestDto.name(), channelType, foundUser, ChannelVisibility.PUBLIC);
        Channel savedChannel = channelRepository.save(createChannel);
        return ChannelCreateResponseDto.from(savedChannel);
    }

    @Override
    public ChannelCreateResponseDto createPrivateChannel(UUID userId) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        Channel createChannel = Channel.ofPrivateChannel(foundUser, ChannelType.TEXT);
        Channel savedChannel = channelRepository.save(createChannel);
        ReadStatus readStatus = new ReadStatus(foundUser, savedChannel);
        readStatusRepository.save(readStatus);
        return ChannelCreateResponseDto.from(savedChannel);
    }

    @Override
    public void joinPublicChannel(UUID invitedUserId, InviteChannelRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(invitedUserId);
        Channel foundChannel = findOneByIdOrThrow(requestDto.channelId());
        foundChannel.join(foundUser);
        channelRepository.save(foundChannel);
    }

    @Override
    public void joinPrivateChannel(UUID invitedUserId, InviteChannelRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(invitedUserId);
        Channel foundChannel = findOneByIdOrThrow(requestDto.channelId());
        foundChannel.join(foundUser);
        ReadStatus readStatus = new ReadStatus(foundUser, foundChannel);
        readStatusRepository.save(readStatus);
        channelRepository.save(foundChannel);
    }

    @Override
    public FoundChannelResponseDto findOneByChannelId(UUID channelId) {
        Channel foundChannel = findOneByIdOrThrow(channelId);
        LocalDateTime lastMessageTime = messageService.getLastMessageTime(foundChannel.getId());
        if (foundChannel.isPublic()) {
            return FoundChannelResponseDto.ofPublicChannel(foundChannel, lastMessageTime);
        }
        else {
            return FoundChannelResponseDto.ofPrivateChannel(foundChannel, lastMessageTime);
        }
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
