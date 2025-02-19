package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.channel.*;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.message.MessageResponse;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.IllegalChannelException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.validate.ChannelServiceValidator;
import com.sprint.mission.discodeit.service.validate.MessageServiceValidator;
import com.sprint.mission.discodeit.service.validate.ServiceValidator;
import com.sprint.mission.discodeit.service.validate.UserServiceValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private static final String CHANNEL_TYPE_PUB = "PUBLIC";
    private static final String CHANNEL_TYPE_PRI = "PRIVATE";
    private static final String SPECIFIC_USER = "ADMIN";

    private final ChannelRepository channelRepository;
    private final MessageService messageService;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private static final EntityFactory entityFactory = BaseEntityFactory.getInstance();
    private static final ServiceValidator<Channel> channelValidator = new ChannelServiceValidator();
    private static final ServiceValidator<User> userValidator = new UserServiceValidator();
    private static final ServiceValidator<Message> messageValidator = new MessageServiceValidator();

    @PostConstruct
    public void init() {
        log.error("주입된 channelRepository: {}", channelRepository.getClass().getSimpleName());
    }


    /**
     * 일반 public 채널 생성
     */
    @Override
    public ChannelResponse createPublicChannel(ChannelCommonRequest request, Map<UUID, User> userList) {
        if (request.getChannelName() == null || request.getOwner() == null) {
            throw new IllegalChannelException();
        }

        // 채널 이름 중복 체크 추가
        validateDuplicateChannelName(request);

        // 채널 자체의 시간 정보
        createChannelReadStatus(request);

        Channel channel = setPublicChannel(request, userList);
        channelRepository.saveChannel(channel);

        return convertToChannelResponse(channel);
    }

    /**
     * private 채널 생성
     */
    @Override
    public ChannelResponse createPrivateChannel(ChannelPrivateRequest privateRequest, Map<UUID, User> userList) {
        if (privateRequest.getOwner() == null || privateRequest.getChannelType().equals(CHANNEL_TYPE_PUB)) {
            throw new IllegalChannelException();
        }

        // 채널 자체의 시간 정보
        ReadStatus channelReadState = new ReadStatus(privateRequest.getChannelId(), privateRequest.getChannelId());
        readStatusRepository.save(channelReadState);

        // 각각 유저에 대한 시간 정보
        userList.values()
                .forEach(entry -> {
                    ReadStatus readStatus = new ReadStatus(entry.getId(), privateRequest.getChannelId());
                    readStatusRepository.save(readStatus);
                });

        Channel channel = setPrivateChannel(privateRequest, userList);
        channelRepository.saveChannel(channel);

        return new ChannelResponse(channel.getId(), channel.getChannelName(), channel.getChannelType(), channel.getChannelOwnerUser());
    }

    /**
     * 채널 ID로 찾기
     */
    @Override
    public ChannelFindResponse findChannelById(UUID channelId) {
        if (channelId == null) {
            throw new IllegalChannelException("채널 아이디가 올바르지 않습니다.");
        }

        Channel findChannel = channelValidator.entityValidate(channelRepository.findChannelById(channelId));

        // 채널에서 가장 최근의 메세지 시간 정보 뽑기
        Map<UUID, ReadStatus> allReadStatus = readStatusRepository.findAll();
        ReadStatus status = findRecentMessageReadStatus(allReadStatus, findChannel);


        if (findChannel.getChannelType().equals(CHANNEL_TYPE_PRI)) {
            return toChannelPrivateResponse(findChannel, status);
        } else {
            return toFindResponse(findChannel, status);
        }

    }

    /**
     * @return Map<UUID, Channel>
     * @Description: channelRepository에서 모든 채널을 가져오기
     */
    @Override
    public Map<UUID, ChannelFindResponse> getAllChannels(UUID userId) {
        // 채널을 findChannelById를 이용해 ChannelFindResponse DTO로 변경
        Map<UUID, ChannelFindResponse> channels = channelRepository.findAllChannel().values().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getId(),
                        entry -> findChannelById(entry.getId())
                ));


        // DTO로 바꾼 새로운 맵을 전부 출력하는데
        // 채널이 public 타입이면 바로 반환
        // private 채널이면 조회한 유저가 참여한 채널만 조회
        return channels.entrySet().stream()
                .filter(entry -> {
                    ChannelFindResponse response = entry.getValue();

                    if (response.getChannelType().equals(CHANNEL_TYPE_PUB)) {
                        return true;
                    } else {
                        Channel channel = channelRepository.findChannelById(entry.getKey());

                        return channel.getChannelUsers().values().stream()
                                .anyMatch(user -> user.getId().equals(userId));
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                ));

    }

    /**
     * 특정 `User`가 볼 수 있는 Channel 목록을 조회하도록 조회 조건
     */
    private Map<UUID, ChannelResponse> findAllByUserId(UUID userId) {
        Map<UUID, Channel> channels = channelRepository.findAllChannel();

        Map<UUID, ChannelResponse> filteredChannels = channels.values().stream()
                .filter(ch -> ch.getChannelUsers().values().stream()
                        .anyMatch(user -> user.getId().equals(userId)))
                .collect(Collectors.toMap(
                        Channel::getId,
                        channel -> new ChannelResponse(channel.getId(), channel.getChannelName(), channel.getChannelType(), channel.getChannelOwnerUser())
                ));

        return filteredChannels;
    }

    /**
     *- [ ] DTO를 활용해 파라미터를 그룹화합니다.
     *   - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
     * - [ ] PRIVATE 채널은 수정할 수 없습니다.
     */
    @Override
    public ChannelUpdateResponse updateChannel(ChannelUpdateRequest request) {
        Channel channel = channelRepository.findChannelById(request.channelUUID());
        channel.updateChannelName(request.channelName());
        channel.setChannelType(request.channelType());
        channel.updateOwnerUser(request.owner());

        if (channel.getChannelType().equals(CHANNEL_TYPE_PRI)) {
            throw new IllegalChannelException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channelRepository.saveChannel(channel);

        return new ChannelUpdateResponse(channel.getId(), channel.getChannelName(), channel.getChannelOwnerUser().getId(), channel.getChannelType());
    }

    /**
     * 채널에 메세지 추가
     */
    @Override
    public void sendMessage(ChannelAddMessageRequest request) {
        Channel findChannel = channelRepository.findChannelById(request.channelId());
        MessageResponse message = messageService.getMessageById(request.messageId());

        if (findChannel == null || message == null) {
            throw new ChannelNotFoundException();
        }

        User sender = userRepository.findUserById(message.senderId());
        User receiver = userRepository.findUserById(message.receiverId());

        Message createdMessage = new Message(message.messageId(), message.title(), message.content(), sender, receiver);
        findChannel.addMessageInChannel(createdMessage);
        channelRepository.saveChannel(findChannel);

    }

    /**
     * 관련된 도메인도 같이 삭제합니다.
     * - `Message`, `ReadStatus`
     */
    @Override
    public void removeChannelById(UUID channelUUID) {
        Channel findChannel = channelRepository.findChannelById(channelUUID);
        // 채널까지 잘 찾음

        Map<UUID, Message> messageMap = Optional.ofNullable(findChannel.getChannelMessages()).orElseThrow(MessageNotFoundException::new);

        // 채널 내 메시지 삭제 (MessageService 사용)
        if (!messageMap.isEmpty()) {
            List<UUID> messageIds = new ArrayList<>(messageMap.keySet());
            for (UUID messageId : messageIds) {
                messageService.deleteMessage(messageId);
            }
        }

        // 4. ReadStatus 삭제 - 새로운 리스트에 삭제할 ID들을 모은 후 한꺼번에 처리
        Map<UUID, ReadStatus> allReadStatuses = readStatusRepository.findAll();
        List<UUID> readStatusesToRemove = new ArrayList<>();

        // 삭제할 ReadStatus의 ID들을 먼저 수집
        for (ReadStatus readStatus : allReadStatuses.values()) {
            if (readStatus.getChannelId().equals(channelUUID)) {
                readStatusesToRemove.add(readStatus.getChannelId());
            }
        }

        // 수집된 ID들을 기반으로 삭제 수행
        for (UUID statusId : readStatusesToRemove) {
            readStatusRepository.remove(statusId);
        }

        // 5. 채널 삭제
        channelRepository.removeChannelById(findChannel.getId());
    }

    private static Channel setPublicChannel(ChannelCommonRequest request, Map<UUID, User> userList) {
        Channel channel = new Channel(request.getChannelId());
        channel.updateChannelName(request.getChannelName());
        channel.updateOwnerUser(request.getOwner());
        channel.setChannelType("PUBLIC");
        channel.updateChannelUsers(userList);
        channel.setChannelMessages(new HashMap<>());
        return channel;
    }

    private static Channel setPrivateChannel(ChannelPrivateRequest privateRequest, Map<UUID, User> userList) {
        Channel channel = new Channel(privateRequest.getChannelId());
        channel.updateChannelName(privateRequest.getChannelName());
        channel.updateOwnerUser(privateRequest.getOwner());
        channel.setChannelType("PRIVATE");
        channel.updateChannelUsers(userList);
        channel.setChannelMessages(new HashMap<>());
        return channel;
    }



    private static ChannelFindPrivateResponse toChannelPrivateResponse(Channel findChannel, ReadStatus status) {
        List<UUID> userIdList = findChannel.getChannelUsers().values().stream()
                .map(entry -> entry.getId())
                .toList();

        return new ChannelFindPrivateResponse(findChannel.getId(),
                findChannel.getChannelName(),
                findChannel.getChannelOwnerUser(),
                findChannel.getChannelType(),
                status,
                userIdList);
    }

    private static ChannelResponse convertToChannelResponse(Channel channel) {
        return new ChannelResponse(channel.getId(), channel.getChannelName(), channel.getChannelType(), channel.getChannelOwnerUser());
    }

    private static ChannelFindResponse toFindResponse(Channel findChannel, ReadStatus status) {
        ChannelFindResponse response = new ChannelFindResponse(findChannel.getId(),
                findChannel.getChannelName(),
                findChannel.getChannelOwnerUser(),
                findChannel.getChannelType(), status);

        return response;
    }

    private void createChannelReadStatus(ChannelCommonRequest request) {
        ReadStatus channelReadState = new ReadStatus(request.getOwner().getId(), request.getChannelId());
        readStatusRepository.save(channelReadState);
    }

    private void validateDuplicateChannelName(ChannelCommonRequest request) {
        boolean isDuplicate = channelRepository.findAllChannel().values().stream()
                .anyMatch(ch -> ch.getChannelName().equals(request.getChannelName()));

        if (isDuplicate) {
            throw new IllegalChannelException("중복된 채널 이름입니다.");
        }
    }

    private static ReadStatus findRecentMessageReadStatus(Map<UUID, ReadStatus> allReadStatus, Channel findChannel) {
        ReadStatus status = allReadStatus.values().stream()
                .filter(entry -> entry.getChannelId().equals(findChannel.getId()))
                .max(Comparator.comparing(ReadStatus::getLastReadAt))
                .orElseThrow(() -> new ChannelNotFoundException("시간 정보를 찾지 못했습니다."));
        return status;
    }


    //        channels.values().stream()
//                .filter(entry -> {
//                    if(entry.getChannelMessages().isEmpty()) {
//                        return false;
//                    }
//                    return entry.getId().equals(channelUUID);
//                })
//                .forEach(entry -> {
//                    entry.getChannelMessages().values().forEach(message -> messageService.deleteMessage(message.getId()));

//                });
    /*@Override
    public void addUserChannel(UUID channelUUID, User addUser) {
        if (addUser == null) {
            throw new UserNotFoundException();
        }

        Channel findChannel = findChannelById(channelUUID);

        findChannel.addUser(addUser);

        channelRepository.saveChannel(findChannel);
    }

    @Override
    public void kickUserChannel(UUID channelUUID, User kickUser) {
        User wantKickUser = userValidator.entityValidate(kickUser);

        Channel findChannel = findChannelById(channelUUID);

        if (findChannel.getChannelOwnerUser().equals(wantKickUser)) {
            findNextOwnerUser(findChannel);
        }

        findChannel.removeUser(wantKickUser);

        channelRepository.saveChannel(findChannel);
    }

    private void findNextOwnerUser(Channel findChannel) {
        Channel channel = channelValidator.entityValidate(findChannel);

        User nextOwnerUser = channel.getChannelUsers().entrySet().stream()
                .filter(entry -> !entry.getValue().equals(findChannel.getChannelOwnerUser()))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(UserNotFoundException::new);

        channel.updateOwnerUser(nextOwnerUser);
        channel.removeUser(nextOwnerUser);
    }

    *//**
     * @param channelId
     * @param removeMessage
     * @Description: 메세지 서비스에서 가져온 메세지를 삭제, getMessageById에서 검증할 것임
     *//*
    @Override
    public void removeMessageInCh(UUID channelId, Message removeMessage) {
        Channel findChannel = findChannelById(channelId);

        Message message = messageValidator.entityValidate(removeMessage);

        findChannel.getChannelMessages().remove(message.getId());

        channelRepository.saveChannel(findChannel);

        messageService.deleteMessage(message.getId());
    }

    *//**
     * @param channelId
     * @param messageId
     * @return Message
     * @Description: 채널에서 특정 메세지를 ID로 찾기
     *//*
    @Override
    public Message findChannelMessageById(UUID channelId, UUID messageId) {
        Channel findChannel = findChannelById(channelId);

        return findChannel.getChannelMessages().entrySet().stream()
                .filter(entry -> entry.getKey().equals(messageId))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(MessageNotFoundException::new);

    }

    @Override
    public Map<UUID, Message> findChannelInMessageAll(UUID channelId) {
        Channel findChannel = findChannelById(channelId);

        return findChannel.getChannelMessages();
    }*/

    //    @Override
//    public Map<UUID, Channel> getChannelByName(String channelName) {
//        Map<UUID, Channel> allChannels = getAllChannels();
//
//        if (allChannels.isEmpty()) {
//            throw new ChannelNotFoundException();
//        }
//
//        return allChannels.entrySet().stream()
//                .filter(entry -> entry.getValue().getChannelName().equals(channelName))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue
//                ));
//    }
}
