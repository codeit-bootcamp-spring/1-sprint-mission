package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelListResponse;
import com.sprint.mission.discodeit.entity.channel.ChannelResponse;
import com.sprint.mission.discodeit.entity.channel.add.ChannelAddMessageRequest;
import com.sprint.mission.discodeit.entity.channel.add.ChannelAddUserResponse;
import com.sprint.mission.discodeit.entity.channel.create.ChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateResponse;
import com.sprint.mission.discodeit.entity.channel.find.ChannelFindOfUserResponse;
import com.sprint.mission.discodeit.entity.channel.find.ChannelFindPrivateResponse;
import com.sprint.mission.discodeit.entity.channel.find.ChannelFindResponse;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyResponse;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelUpdateResponse;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.message.MessageResponse;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserChannelOwnerResponse;
import com.sprint.mission.discodeit.entity.user.UserRole;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.IllegalChannelException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


  @Override
  public ChannelResponse createChannel(ChannelCreateRequest request) {
    User owner = userRepository.findUserByName(request.ownerName());

    if (owner == null) {
      throw new UserNotFoundException();
    }

    ChannelResponse channelCreateRequest = new ChannelResponse(
        UUID.randomUUID(),
        request.channelName(),
        request.type(),
        new UserChannelOwnerResponse(owner.getId(), owner.getUserName(), owner.getUserRole()));

    return getUserChannelResponse(channelCreateRequest);
  }


  /**
   * 일반 public 채널 생성
   */
  @Override
  public ChannelResponse createPublicChannel(ChannelCreateRequest request,
      Map<UUID, User> userList) {
    if (request.channelName() == null || request.ownerName() == null) {
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
   * 스프린트 미션 5 심화 요구사항을 위한 PUBLIC 채널 생성 로직
   */
  public PublicChannelCreateResponse createPublicChannel(PublicChannelCreateRequest request) {

    Channel channel = Channel.builder()
        .channelName(request.name())
        .channelType(CHANNEL_TYPE_PUB)
        .description(request.description())
        .build();

    channelRepository.saveChannel(channel);

    return getPublicChannelCreateResponse(channel);
  }


  /**
   * 스프린트 미션 5 심화 요구사항을 위한 **PRIVATE** 채널 생성 로직
   */
  @Override
  public PrivateChannelCreateResponse createPrivateChannel(PrivateChannelCreateRequest request) {
    Channel channel = Channel.builder()
        .channelType(CHANNEL_TYPE_PRI)
        .build();
    channel.getParticipantIds().addAll(request.participantIds());

    request.participantIds().forEach(uuid -> {
      readStatusRepository.save(new ReadStatus(uuid, channel.getId()));
    });

    channelRepository.saveChannel(channel);

    return new PrivateChannelCreateResponse(channel.getId(), channel.getCreatedAt(),
        channel.getUpdatedAt(), channel.getChannelType(), channel.getChannelName(),
        channel.getDescription());
  }

  /**
   * private 채널 생성
   */
  @Override
  public ChannelResponse createPrivateChannel(ChannelCreateRequest privateRequest,
      Map<UUID, User> userList) {
    if (privateRequest.ownerName() == null || privateRequest.type().equals(CHANNEL_TYPE_PUB)) {
      throw new IllegalChannelException();
    }

    // 채널 자체의 시간 정보
    ReadStatus channelReadState = new ReadStatus(privateRequest.channelId(),
        privateRequest.channelId());
    readStatusRepository.save(channelReadState);

    // 각각 유저에 대한 시간 정보
    userList.values()
        .forEach(entry -> {
          ReadStatus readStatus = new ReadStatus(entry.getId(), privateRequest.channelId());
          readStatusRepository.save(readStatus);
        });

    Channel channel = setPrivateChannel(privateRequest, userList);
    channelRepository.saveChannel(channel);

    UserChannelOwnerResponse userChannelOwnerResponse = getUserChannelOwnerResponse(privateRequest);

    return new ChannelResponse(channel.getId(), channel.getChannelName(), channel.getChannelType(),
        userChannelOwnerResponse);
  }

  /**
   * 채널에 유저 추가
   */
  @Override
  public ChannelAddUserResponse addUserChannel(UUID channelUUID, String username) {
    Channel findChannel = channelRepository.findChannelById(channelUUID);
    User findUser = userRepository.findUserByName(username);

    addUserInChannel(findChannel, findUser);

    Channel savedChannel = channelRepository.saveChannel(findChannel);

    return new ChannelAddUserResponse(savedChannel.getId(), savedChannel.getChannelName(),
        findUser.getUserName());
  }

  /**
   * 채널 ID로 찾기
   */
  @Override
  public ChannelFindResponse findChannelById(UUID channelId) {
    if (channelId == null) {
      throw new IllegalChannelException("채널 아이디가 올바르지 않습니다.");
    }

    Channel findChannel = channelValidator.entityValidate(
        channelRepository.findChannelById(channelId));

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
    Map<UUID, ChannelFindResponse> channels = convertToChannelFindResponseMap();

    // DTO로 바꾼 새로운 맵을 전부 출력하는데
    // 채널이 public 타입이면 바로 반환
    // private 채널이면 조회한 유저가 참여한 채널만 조회
    return getFindChannels(userId, channels);
  }

  @Override
  public Map<UUID, ChannelListResponse> getAllChannelsOfUser(String userName) {
    User findUser = userRepository.findUserByName(userName);
    Map<UUID, ChannelFindResponse> allChannels = getAllChannels(findUser.getId());

    // ChannelFindResponse를 ChannelListResponse로 바뀐 뒤 리턴
    return allChannels.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> new ChannelListResponse(
                entry.getValue().getChannelId(),
                entry.getValue().getChannelName(),
                entry.getValue().getChannelType()
            )
        ));
  }

  /**
   * 스프린트 미션 5, 유저가 참여중인 channel 목록 조회
   */
  @Override
  public List<ChannelFindOfUserResponse> findAllChannelsFindByUserId(UUID userId) {
    List<ChannelFindOfUserResponse> responses = new ArrayList<>();
    Map<UUID, Channel> channel = channelRepository.findAllChannel();

    setChannelList(userId, channel, responses);

    return responses;
  }

  private void setChannelList(UUID userId, Map<UUID, Channel> channel,
      List<ChannelFindOfUserResponse> responses) {

    responses.addAll(
        channel.values().stream()
            .filter(chan -> chan.getParticipantIds().contains(userId))
            .map(ch -> new ChannelFindOfUserResponse(
                ch.getId(),
                ch.getChannelType(),
                ch.getChannelName(),
                ch.getDescription(),
                ch.getParticipantIds()
            ))
            .toList()
    );

  }

  /**
   * - [ ] DTO를 활용해 파라미터를 그룹화합니다. - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터 - [ ] PRIVATE 채널은 수정할 수 없습니다.
   */
  @Override
  public ChannelUpdateResponse updateChannel(String channelName, ChannelUpdateRequest request) {
    Channel channel = channelRepository.findChannelByName(channelName);
    User owner = userRepository.findUserByName(request.ownerName());

    channel.updateChannelName(request.channelName());
    channel.setChannelType(request.channelType());
    channel.updateOwnerUser(owner);

    if (channel.getChannelType().equals(CHANNEL_TYPE_PRI)) {
      throw new IllegalChannelException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    channelRepository.saveChannel(channel);

    return new ChannelUpdateResponse(channel.getId(), channel.getChannelName(),
        channel.getChannelOwnerUser().getId(), channel.getChannelType());
  }

  /**
   * 스프린트 미션 5, 채널 수정
   */
  @Override
  public ChannelModifyResponse modifyChannel(UUID channelId, ChannelModifyRequest request) {
    Channel findChannel = channelRepository.findChannelById(channelId);
    if (findChannel == null) {
      throw new ChannelNotFoundException();
    }

    if (findChannel.getChannelType().equals("PRIVATE")) {
      throw new IllegalChannelException("Private channel cannot be updated");
    }

    findChannel.modifyChannel(request.newName(), request.newDescription());
    channelRepository.saveChannel(findChannel);

    return new ChannelModifyResponse(findChannel.getId(), findChannel.getCreatedAt(),
        findChannel.getUpdatedAt(), findChannel.getChannelType(), findChannel.getChannelName(),
        findChannel.getDescription());
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

    Message createdMessage = new Message(message.messageId(), message.messageTitle(),
        message.messageContent(), sender, receiver);
    findChannel.addMessageInChannel(createdMessage);
    channelRepository.saveChannel(findChannel);

  }

  /**
   * 관련된 도메인도 같이 삭제합니다. - `Message`, `ReadStatus`
   */
  @Override
  public UUID removeChannelById(UUID channelUUID) {
    Channel findChannel = channelRepository.findChannelById(channelUUID); // 여기서 채널을 못찾음

    Map<UUID, Message> messageMap = Optional.ofNullable(findChannel.getChannelMessages())
        .orElseThrow(MessageNotFoundException::new);

    // 채널 내 메시지 삭제 (MessageService 사용)
    if (!messageMap.isEmpty()) {
      List<UUID> messageIds = new ArrayList<>(messageMap.keySet());
      for (UUID messageId : messageIds) {
        messageService.deleteMessage(String.valueOf(messageId));
      }
    }

    // 4. ReadStatus 삭제 - 새로운 리스트에 삭제할 ID들을 모은 후 한꺼번에 처리
    Map<UUID, ReadStatus> allReadStatuses = readStatusRepository.findAll();

    if (!allReadStatuses.isEmpty()) {
      List<UUID> readStatusesToRemove = new ArrayList<>();

      for (ReadStatus readStatus : allReadStatuses.values()) {
        if (readStatus.getChannelId().equals(channelUUID)) {
          readStatusesToRemove.add(readStatus.getChannelId());
        }
      }

      for (UUID statusId : readStatusesToRemove) {
        readStatusRepository.remove(statusId);
      }

    }

    // 5. 채널 삭제
    channelRepository.removeChannelById(findChannel.getId());

    return channelUUID;
  }

  @Override
  public UUID removeChannelByName(String channelName) {
    Channel findChannel = channelRepository.findChannelByName(channelName);

    removeChannelById(findChannel.getId());

    return findChannel.getId();
  }

  private Channel setPublicChannel(ChannelCreateRequest request, Map<UUID, User> userList) {
    return buildChannel(request, "PUBLIC", userList);
  }

  private Map<UUID, ChannelFindResponse> getFindChannels(UUID userId,
      Map<UUID, ChannelFindResponse> channels) {
    return channels.entrySet().stream()
        .filter(entry -> {
          ChannelFindResponse response = entry.getValue();

          return checkPublicOrPrivateChannel(userId, entry, response);
        })
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue
        ));
  }

  /**
   * 특정 `User`가 볼 수 있는 Channel 목록을 조회하도록 조회 조건
   */
  private Map<UUID, ChannelResponse> findAllByUserId(UUID userId) {
    Map<UUID, Channel> channels = channelRepository.findAllChannel();

    return channels.values().stream()
        .filter(ch -> ch.getChannelUsers().values().stream()
            .anyMatch(user -> user.getId().equals(userId)))
        .collect(Collectors.toMap(
            Channel::getId,
            channel -> new ChannelResponse(channel.getId(), channel.getChannelName(),
                channel.getChannelType(),
                new UserChannelOwnerResponse(channel.getChannelOwnerUser().getId(),
                    channel.getChannelOwnerUser().getUserName(),
                    channel.getChannelOwnerUser().getUserRole()))
        ));
  }

  private boolean checkPublicOrPrivateChannel(UUID userId,
      Map.Entry<UUID, ChannelFindResponse> entry, ChannelFindResponse response) {
    if (response.getChannelType().equals(CHANNEL_TYPE_PUB)) { // 퍼블릭 채널이면 그냥 반환
      return true;
    } else {
      Channel channel = channelRepository.findChannelById(entry.getKey());

      return userId.equals(channel.getChannelOwnerUser().getId()) ||
          channel.getChannelUsers().values().stream()
              .anyMatch(user -> user.getId().equals(userId));
    }
  }

  private Channel setPrivateChannel(ChannelCreateRequest privateRequest, Map<UUID, User> userList) {
    return buildChannel(privateRequest, "PRIVATE", userList);
  }

  private Map<UUID, ChannelFindResponse> convertToChannelFindResponseMap() {
    return channelRepository.findAllChannel().values().stream()
        .collect(Collectors.toMap(
            entry -> entry.getId(),
            entry -> findChannelById(entry.getId())
        ));
  }

  private UserChannelOwnerResponse getUserChannelOwnerResponse(
      ChannelCreateRequest privateRequest) {
    User owner = userRepository.findUserByName(privateRequest.ownerName());
    return new UserChannelOwnerResponse(owner.getId(), owner.getUserName(), owner.getUserRole());
  }

  private Channel buildChannel(ChannelCreateRequest privateRequest, String channelType,
      Map<UUID, User> userList) {
    Channel channel = new Channel(privateRequest.channelId());
    channel.updateChannelName(privateRequest.channelName());
    channel.updateOwnerUser(userRepository.findUserByName(privateRequest.ownerName()));
    channel.setChannelType(channelType);
    channel.updateChannelUsers(userList);
    channel.getChannelOwnerUser().updateUserRole(UserRole.ROLE_ADMIN);
    channel.setChannelMessages(new HashMap<>());
    return channel;
  }


  private static ChannelFindPrivateResponse toChannelPrivateResponse(Channel findChannel,
      ReadStatus status) {
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
    return new ChannelResponse(channel.getId(), channel.getChannelName(), channel.getChannelType(),
        new UserChannelOwnerResponse(channel.getChannelOwnerUser().getId(),
            channel.getChannelOwnerUser().getUserName(),
            channel.getChannelOwnerUser().getUserRole()));
  }

  private static ChannelFindResponse toFindResponse(Channel findChannel, ReadStatus status) {
    ChannelFindResponse response = new ChannelFindResponse(findChannel.getId(),
        findChannel.getChannelName(),
        findChannel.getChannelOwnerUser(),
        findChannel.getChannelType(), status);

    return response;
  }

  private void createChannelReadStatus(ChannelCreateRequest request) {
    User findUser = userRepository.findUserByName(request.ownerName());
    ReadStatus channelReadState = ReadStatus.createReadStatus(findUser.getId(),
        request.channelId());
    readStatusRepository.save(channelReadState);
  }

  private void validateDuplicateChannelName(ChannelCreateRequest request) {
    boolean isDuplicate = channelRepository.findAllChannel().values().stream()
        .anyMatch(ch -> ch.getChannelName().equals(request.channelName()));

    if (isDuplicate) {
      throw new IllegalChannelException("중복된 채널 이름입니다.");
    }
  }

  private static void addUserInChannel(Channel findChannel, User findUser) {
    if (!findChannel.getChannelOwnerUser().equals(findUser)) {
      findUser.updateUserRole(UserRole.ROLE_COMMON);
    }

    findChannel.addUser(findUser);
  }

  private static PublicChannelCreateResponse getPublicChannelCreateResponse(Channel channel) {
    return new PublicChannelCreateResponse(channel.getId(), channel.getCreatedAt(),
        channel.getUpdatedAt(), channel.getChannelType(), channel.getChannelName(),
        channel.getDescription());
  }

  private ChannelResponse getUserChannelResponse(ChannelResponse request) {
    ChannelResponse response;
    ChannelCreateRequest channelCreateRequest = new ChannelCreateRequest(request.channelId(),
        request.channelName(), request.owner().userName(), request.channelType());

    if (request.channelType().equals("PUBLIC")) {
      response = createPublicChannel(channelCreateRequest, new HashMap<>());
    } else if (request.channelType().equals("PRIVATE")) {
      response = createPrivateChannel(channelCreateRequest, new HashMap<>());
    } else {
      throw new IllegalChannelException();
    }

    log.warn("채널 생성 [{}][{}]", response.channelType(), response.channelName());

    return response;
  }

  private static ReadStatus findRecentMessageReadStatus(Map<UUID, ReadStatus> allReadStatus,
      Channel findChannel) {
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
