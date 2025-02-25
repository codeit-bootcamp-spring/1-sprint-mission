package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.aspect.UpdateUserStatus;
import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.dto.channel.CreateChannel;
import com.sprint.mission.discodeit.dto.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.channel.UpdatePublicChannel;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.type.ChannelType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService  {
    private final MessageService messageService;

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;

    @Override
    public Channel createPublicChannel(CreateChannel.PublicRequest request) {
        // User가 UserRepository에 저장되어 있는지 확인
        for (UUID userID : request.getJoinUser()) {
            Optional<User> userOptional = userRepository.findById(userID);
            if (userOptional.isEmpty()) {
                throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
            }
        }

        Channel channel = new PublicChannel(request.getName(), request.getDescription(), request.getChannelFormat(), request.getJoinUser());
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel createPrivateChannel(CreateChannel.PrivateRequest request) {
        List<UUID> users = request.getJoinUser();

        // User가 UserRepository에 저장되어 있는지 확인
        for (UUID userID : users) {
            Optional<User> userOptional = userRepository.findById(userID);
            if (userOptional.isEmpty()) {
                throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
            }
        }
        // Private 채널 생성 후 저장
        Channel channel = new PrivateChannel(users, request.getChannelFormat());
        channelRepository.save(channel);

        // User마다 ReadStatus 생성하기
        for (UUID userID : users) {
            Instant lastReadAt = Instant.now();
            ReadStatus readStatus = new ReadStatus(userID, channel.getId(), lastReadAt);
            readStatusRepository.save(readStatus);
        }

        return channel;
    }

    @Override
    public ChannelDTO.PublicChannelDTO findPublicChannel(UUID channelId) {
        PublicChannel findChannel = (PublicChannel) channelRepository.findById(channelId).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));

        Message lastestMessage = messageRepository.findLatestByChannelId(channelId).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));
        return ChannelDTO.PublicChannelDTO.fromDomain(findChannel, lastestMessage.getCreatedAt());
    }

    @Override
    public ChannelDTO.PrivateChannelDTO findPrivateChannel(UUID channelId) {
        PrivateChannel findChannel = (PrivateChannel) channelRepository.findById(channelId).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
        if (findChannel.getChannelType().equals(ChannelType.PUBLIC)) {
            throw new ServiceException(ErrorCode.CHANNEL_TYPE_MISMATCH);
        }

        // 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
        Instant latestMessageTime = Instant.EPOCH; // 초기값 설정
        List<Message> messageList = messageRepository.findAll();

        for (Message message : messageList) {
            if (message.getChannelID().equals(findChannel.getId())) {
                if (message.getCreatedAt().isAfter(latestMessageTime)) {
                    latestMessageTime = message.getCreatedAt();
                }
            }
        }

        //Private 채널인 경우 참여한 User의 Id 정보를 포함합니다.
        return ChannelDTO.PrivateChannelDTO.fromDomain(findChannel, latestMessageTime);
    }

    @UpdateUserStatus
    @Override
    public List<Channel> findAllPrivate(UUID userId) { // 통일성을 위해 findAllByUserId보다는 findAllPrivate으로 하였습니다.
        // 해당 userId를 갖는 User가 repository에 저장되어 있는지 확인하기
        validUser(userId);

        // Private 채널 내에서 userId가 같은 회원을 joinMembers 리스트에 갖고 있는 채널만 선택한다.
        List<Channel> allChannels = channelRepository.findAll();
        return allChannels.stream()
                .filter(channel -> channel instanceof PrivateChannel)
                .filter(channel -> channel.getJoinMembers().stream()
                        .anyMatch(userID -> userID.equals(userId)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UpdatePublicChannel request) { // 채널에 새로운 유저 참여, 채널 이름, 설명 수정 가능
        validChannel(request.channelId());
        validUser(request.newUserID());

        PublicChannel updateChannel = (PublicChannel) channelRepository.findById(request.channelId()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
        updateChannel.update(request.name(), request.description(), request.newUserID());

        channelRepository.save(updateChannel);
        return updateChannel;
    }

    @Override
    public Channel deletePrivate(UUID channelId) {
        validChannel(channelId);

        Channel removeChannel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));

        // 채널에 포함된 메시지 삭제하기
        List<Message> messages = messageService.findAllByChannelId(channelId);
        if (!messages.isEmpty()) {
            messages.forEach(message -> messageRepository.delete(message));
        }

        // 채널에 포함된 ReadStatus 삭제하기
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);
        readStatusRepository.deleteAll(readStatuses);

        channelRepository.delete(removeChannel);
        return removeChannel;
    }

    @Override
    public Channel deletePublic(UUID channelId) {
        validChannel(channelId);

        Channel removeChannel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));

        // 채널에 포함된 메시지 삭제하기
        List<Message> messages = messageService.findAllByChannelId(channelId);
        if (!messages.isEmpty()) {
            messages.forEach(message -> messageRepository.delete(message));
        }

        channelRepository.delete(removeChannel);
        return removeChannel;
    }

    // 채널 검증
    private void validChannel(UUID channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
    }

    // 유저 검증
    private void validUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
    }
}
