package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelCategory;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;

  private final UserRepository userRepository;

  private final MessageRepository messageRepository;

  private final ReadStatusRepository readStatusRepository;

  private final UserStatusRepository userStatusRepository;

  @Override
  public ChannelDto create(CreatePublicChannelDto createPublicChannelDto)
      throws CustomException {

    if (createPublicChannelDto == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    Channel channel = channelRepository.save(
        new Channel(createPublicChannelDto.name(), ChannelType.PUBLIC,
            ChannelCategory.TEXT, createPublicChannelDto.description()));
    return ChannelDto.from(channel, null, null);
  }

  @Override
  public ChannelDto create(CreatePrivateChannelDTo createPrivateChannelDTo) {
    if (createPrivateChannelDTo == null || createPrivateChannelDTo.participantIds().isEmpty()) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    Channel channel = channelRepository.save(
        new Channel(null, ChannelType.PRIVATE, ChannelCategory.TEXT,
            null));

    List<String> userIds = createPrivateChannelDTo.participantIds().stream().distinct().toList();
    List<UserDto> participants = new ArrayList<>();

    for (String userId : userIds) {
      User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
      if (user == null) {
        throw new CustomException(ErrorCode.USER_NOT_FOUND);
      }
      ReadStatus readStatus = new ReadStatus(channel, user, Instant.now());
      readStatusRepository.save(readStatus);
      participants.add(UserDto.from(user, user.getUserStatus().isActive()));
    }
    Channel savedChannel = channelRepository.save(channel);
    return ChannelDto.from(savedChannel, null, participants);
  }

  @Override
  public List<ChannelDto> findAllByUserId(String userId) {
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    // readStatus로 유저가 어느 채널을 가지고 있는지 알 수 있음
    // readStatus로 얻은 Channel들 리스트
    // 그 Channel이 만약 public이면 사용자 목록 안담아도 됨
    // Private이면 해당 channel의 id를 가지고 readStatus를 모두 조회해서 각채널에 user 담아야함
    List<Channel> channels = readStatusRepository
        .findByUserId(UUID.fromString(userId)).stream().map(r -> r.getChannel()).toList();

    List<ChannelDto> channelDtos = new ArrayList<>();
    for (Channel channel : channels) {
      List<UserDto> participants = null;
      if (channel.getChannelType() == ChannelType.PRIVATE) {
        participants = readStatusRepository.findByChannelId(channel.getId())
            .stream()
            .map(r -> UserDto.from(r.getUser(),
                userStatusRepository.findByUser(r.getUser()).orElse(null).isActive())).toList();
      }
      channelDtos.add(ChannelDto.from(channel, getLastMessageTimestamp(channel), participants));
    }
    return channelDtos;
  }

  @Override
  public List<MessageDto> findAllMessagesByChannelId(String channelId) {
    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElse(null);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    return messageRepository.findByChannelId(channelId).stream().map(MessageDto::from)
        .toList();
  }

  @Override
  public ChannelDto findById(String channelId, String userId) throws CustomException {
    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElse(null);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
        channel.getId(), UUID.fromString(userId)).orElse(null);

    if (readStatus == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    List<UserDto> participants = List.of();
    if (channel.getChannelType() == ChannelType.PRIVATE) {
      participants = readStatusRepository.findByChannelId(channel.getId()).stream().map(
          r -> UserDto.from(r.getUser(),
              userStatusRepository.findByUser(r.getUser()).orElse(null).isActive())).toList();
    }

    return ChannelDto.from(channel, getLastMessageTimestamp(channel), participants);
  }


  @Override
  public ChannelDto updateChannel(String channelId, UpdateChannelDto updateChannelDto)
      throws CustomException {

    //dto가 비어있는 경우, 채널 조회를 수행하지 않도록 수정
    if (updateChannelDto == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElse(null);

    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    if (channel.getChannelType() == ChannelType.PRIVATE) {
      throw new CustomException(ErrorCode.CHANNEL_PRIVATE_NOT_UPDATABLE);
    }

    // 변경사항이 있는 경우에만 업데이트 시간 설정
    if (channel.isUpdated(updateChannelDto)) {
      channel.setUpdatedAt(updateChannelDto.updatedAt());
    }

    channelRepository.save(channel);

    List<UserDto> participants = List.of();
    if (channel.getChannelType() == ChannelType.PRIVATE) {
      participants = readStatusRepository.findByChannelId(channel.getId()).stream().map(
          r -> UserDto.from(r.getUser(),
              userStatusRepository.findByUser(r.getUser()).orElse(null).isActive())).toList();
    }

    return ChannelDto.from(channel, getLastMessageTimestamp(channel), participants);
  }

  @Override
  public boolean delete(String channelId) throws CustomException {
    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElse(null);

    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    //레포지토리에서 한번에 삭제 할 수 있는 방법이 있을끼?
    //대용량 서비스라면? -> 삭제 완료될 때까지 기다려야함
    messageRepository.findByChannelId(channelId)
        .forEach(m -> messageRepository.delete(m));
    readStatusRepository.findByChannelId(UUID.fromString(channelId))
        .forEach(rs -> readStatusRepository.delete(rs));

    channelRepository.delete(channel);
    return true;
  }
  //미사용 메서드 임시 주석처리

//  @Override
//  public boolean addUserToChannel(String channelId, String userId) throws CustomException {
//    //todo - set 노출 수정 - how?
//    //여기 검사하는 로직 if문으로 판별하도록 전부 수정하기
//
//    try {
//      //해당 채널이 DB에 존재하는 채널인지 검사
//      Channel c = channelRepository.findById(channelId);
//      //해당 유저가 DB에 존재하는 유저인지 검사
//      User u = userRepository.findById(userId);
//      channelRepository.save(c);
//      return true;
//      //todo - 만약 API 서버라고 가정, 사용자가 요청했을 때 채널이 없어서 API 호출이 실패할텐데 이를 어떻게 알려줄 수 있을지?
//    } catch (CustomException e) {
//      if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
//        System.out.println(
//            "Failed to add User to this channel. User with id " + userId + " not found");
//      } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
//        System.out.println(
//            "Failed to add User to this channel. Channel with id " + channelId + " not found");
//      }
//    }
//    return false;
//  }

//  @Override
//  public boolean deleteUserFromChannel(String channelId, String userId) {
//    Channel channel = channelRepository.findById(channelId);
//    User user = userRepository.findById(userId);
//    if (channel.getUserSet().contains(user.getId().toString())) {
//      channel.getUserSet().remove(user.getId().toString());
//      return true;
//    }
//    return false;
//  }
//
//  @Override
//  public boolean isUserInChannel(String channelId, String userId) {
//    Channel channel = channelRepository.findById(channelId);
//    User user = userRepository.findById(userId);
//    return channel.getUserSet().contains(user.getId().toString());
//  }

  public Instant getLastMessageTimestamp(Channel channel) throws CustomException {
    //todo
    //이것도 레포지토리 쪽으로 책임 넘기기
    return messageRepository.findByChannelId(channel.getId().toString()).stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo).orElse(null);
  }
}
