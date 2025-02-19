package com.sprint.mission.discodeit.application.service.channel;

import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelNameRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelSubjectRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.CreateChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.DeleteChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.FoundChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.InviteChannelRequestDto;
import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.enums.ChannelVisibility;
import com.sprint.mission.discodeit.domain.channel.exception.AlreadyJoinUserException;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.exception.NotChannelManagerException;
import com.sprint.mission.discodeit.domain.readStatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import com.sprint.mission.discodeit.repository.message.interfaces.MessageRepository;
import com.sprint.mission.discodeit.repository.readstatus.interfaces.ReadStatusRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    public JCFChannelService(
            ChannelRepository channelRepository,
            UserService userService,
            ReadStatusRepository readStatusService,
            MessageRepository messageRepository
    ) {
        this.channelRepository = channelRepository;
        this.userService = userService;
        this.readStatusRepository = readStatusService;
        this.messageRepository = messageRepository;
    }

    @Override
    public ChannelCreateResponseDto createPublicChannel(UUID userId, CreateChannelRequestDto requestDto) {
        Channel savedChannel = createChannelWithVisibility(userId, requestDto);
        return ChannelCreateResponseDto.from(savedChannel);
    }

    // 같은 기능의 중복 메서드 => 요구사항에 두 개로 나누어 두라고 했지만, 로직이 매우 비슷.
    @Override
    public ChannelCreateResponseDto createPrivateChannel(UUID userId, CreateChannelRequestDto requestDto) {
        Channel savedChannel = createChannelWithVisibility(userId, requestDto);
        return ChannelCreateResponseDto.from(savedChannel);
    }

    @Override
    public void joinChannel(UUID invitedUserId, InviteChannelRequestDto requestDto) {
        User foundUser = userService.findOneByUserIdOrThrow(invitedUserId);
        Channel foundChannel = findOneByChannelIdOrThrow(requestDto.channelId());
        throwIsAlreadyJoinUser(foundUser, foundChannel);
        foundChannel.join(foundUser);
        createAndSaveReadStatus(foundUser, foundChannel);
        channelRepository.save(foundChannel);
    }

    @Override
    public FoundChannelResponseDto findOneByChannelId(UUID channelId) {
        Channel foundChannel = findOneByChannelIdOrThrow(channelId);
        return toFoundChannelResponseDto(foundChannel);
    }

    public List<FoundChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAllByUserId(userId);
        // 채널 한 개당 메세지를 조회해오는 N + 1
        return channels.stream().map(this::toFoundChannelResponseDto).toList();
    }

    @Override
    public Channel findOneByChannelIdOrThrow(UUID id) {
        return channelRepository.findOneById(id)
                .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Override
    public void changeSubject(UUID userId, ChangeChannelSubjectRequestDto requestDto) {
        User foundUser = userService.findOneByUserIdOrThrow(userId);
        Channel foundChannel = findOneByChannelIdOrThrow(requestDto.channelId());
        throwIsNotManager(foundUser, foundChannel);
        foundChannel.updateSubject(requestDto.subject());
        channelRepository.save(foundChannel);
    }

    @Override
    public void changeChannelName(UUID userId, ChangeChannelNameRequestDto requestDto) {
        User foundUser = userService.findOneByUserIdOrThrow(userId);
        Channel foundChannel = findOneByChannelIdOrThrow(requestDto.channelId());
        throwIsNotManager(foundUser, foundChannel);
        foundChannel.updateName(requestDto.channelName());
        channelRepository.save(foundChannel);
    }

    @Override
    public void deleteChannel(UUID userId, DeleteChannelRequestDto requestDto) {
        User foundUser = userService.findOneByUserIdOrThrow(userId);
        Channel foundChannel = findOneByChannelIdOrThrow(requestDto.channelId());
        throwIsNotManager(foundUser, foundChannel);
        readStatusRepository.deleteByChannel(foundChannel);
        messageRepository.deleteByChannel(foundChannel);
        channelRepository.deleteById(foundChannel.getId());
    }

    private void throwIsNotManager(User foundUser, Channel foundChannel) {
        if (!foundChannel.isManager(foundUser)) {
            throw new NotChannelManagerException(ErrorCode.CHANNEL_ADMIN_REQUIRED, foundUser.getUsernameValue());
        }
    }

    private FoundChannelResponseDto toFoundChannelResponseDto(Channel foundChannel) {
        LocalDateTime lastMessageTime = messageRepository.getLastMessageTimeByChannelId(foundChannel.getId());
        if (foundChannel.isPublic()) {
            return FoundChannelResponseDto.ofPublicChannel(foundChannel, lastMessageTime);
        } else {
            return FoundChannelResponseDto.ofPrivateChannel(foundChannel, lastMessageTime);
        }
    }

    private void throwIsAlreadyJoinUser(User targetUser, Channel targetChannel) {
        if (channelRepository.isExistUser(targetUser, targetChannel)) {
            throw new AlreadyJoinUserException(ErrorCode.ALREADY_CHANNEL_JOIN_USER, targetUser.getNicknameValue());
        }
    }

    private void createAndSaveReadStatus(User foundUser, Channel savedChannel) {
        ReadStatus readStatus = new ReadStatus(foundUser, savedChannel);
        readStatusRepository.save(readStatus);
    }

    private Channel createChannelWithVisibility(UUID userId, CreateChannelRequestDto requestDto) {
        User foundUser = userService.findOneByUserIdOrThrow(userId);
        Channel createChannel = null;
        if (requestDto.visibility() == ChannelVisibility.PUBLIC) {
            createChannel = Channel.ofPublicChannel(requestDto.name(), requestDto.channelType(), foundUser);
        } else if (requestDto.visibility() == ChannelVisibility.PRIVATE) {
            createChannel = Channel.ofPrivateChannel(foundUser, requestDto.channelType());
        }
//        ====> 삼항 연산자와 if 문 분기처리 중 어떤 것을 더 선호해야할까요? 제가 생각하기에는 삼항 연산자가 보기 더 편한것 같습니다.
//        다음 미션에서는 둘 중 하나 지워두겠습니다.
//        createChannel = requestDto.visibility() == ChannelVisibility.PUBLIC
//                ? Channel.ofPublicChannel(requestDto.name(), requestDto.channelType(), foundUser)
//                : Channel.ofPrivateChannel(foundUser, requestDto.channelType());
        Channel savedChannel = channelRepository.save(createChannel);
        createAndSaveReadStatus(foundUser, savedChannel);
        return savedChannel;
    }
}
