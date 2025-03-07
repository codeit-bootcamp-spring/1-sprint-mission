package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelCategory;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserStatusService;
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

  private final ReadStatusService readStatusService;

  private final UserStatusService userStatusService;

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

    for (String userId : userIds) {
      User user = userRepository.findById(userId);
      readStatusService.create(
          new CreateReadStatusDto(channel.getId().toString(), userId, Instant.now()));
      channel.getUserSet().add(user.getId().toString());
    }
    Channel savedChannel = channelRepository.save(channel);
    return ChannelDto.from(savedChannel, null, savedChannel.getUserSet().stream().toList());
  }

  @Override
  public List<ChannelDto> findAllByUserId(String userId) {
    User user = userRepository.findById(userId);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    List<Channel> channels = channelRepository.findByParticipantId(userId);

    return channels.stream().map(c -> ChannelDto.from(c, getLastMessageTimestamp(c),
        c.getUserSet().stream().toList())).toList();
  }

  @Override
  public List<MessageDto> findAllMessagesByChannelId(String channelId) {
    Channel channel = channelRepository.findById(channelId);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    return messageRepository.findAllByChannelId(channelId).stream().map(MessageDto::from)
        .toList();
  }

  @Override
  public ChannelDto findById(String channelId, String userId) throws CustomException {
    Channel channel = channelRepository.findById(channelId);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    //해당 채널이 private인 경우 조회하는 user가 속해있는지 검사
    if (!channel.getUserSet().contains(userId)) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);// todo-에러코드 적절한걸로 수정해야함
    }

    List<String> userIds = (channel.getChannelType() == ChannelType.PRIVATE ? channel.getUserSet()
        .stream().toList() : null);

    return ChannelDto.from(channel, getLastMessageTimestamp(channel), userIds);
  }

  @Override
  public List<ChannelDto> findAllByChannelName(String channelName) throws CustomException {
    List<Channel> channels = channelRepository.findAll().stream()
        .filter(c -> c.getChannelName().contains(channelName)).toList();
    List<ChannelDto> channelDtos = new ArrayList<>();
    for (Channel channel : channels) {
      channelDtos.add(ChannelDto.from(channel, getLastMessageTimestamp(channel),
          channel.getUserSet().stream().toList()));
    }
    return channelDtos;
  }

  @Override
  public List<ChannelDto> findByChannelType(ChannelType channelType) {
    List<Channel> channels = channelRepository.findAll().stream()
        .filter(c -> c.getChannelType().equals(channelType)).toList();
    List<ChannelDto> channelDtos = new ArrayList<>();
    for (Channel channel : channels) {
      channelDtos.add(ChannelDto.from(channel, getLastMessageTimestamp(channel),
          channel.getUserSet().stream().toList()));
    }
    return channelDtos;

  }

  @Override
  public ChannelDto updateChannel(String channelId, UpdateChannelDto updateChannelDto)
      throws CustomException {

    //dto가 비어있는 경우, 채널 조회를 수행하지 않도록 수정
    if (updateChannelDto == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    Channel channel = channelRepository.findById(channelId);

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

    return ChannelDto.from(channel, getLastMessageTimestamp(channel),
        channel.getUserSet().stream().toList());
  }

  @Override
  public boolean delete(String channelId) throws CustomException {
    Channel channel = channelRepository.findById(channelId);

    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    //레포지토리에서 한번에 삭제 할 수 있는 방법이 있을끼?
    //대용량 서비스라면? -> 삭제 완료될 때까지 기다려야함
    messageRepository.findAllByChannelId(channelId)
        .forEach(m -> messageRepository.delete(m.getId().toString()));
    readStatusService.findAllByChannelId(channelId)
        .forEach(rs -> readStatusService.delete(rs.id()));

    return channelRepository.delete(channel.getId().toString());
  }

  @Override
  public List<UserDto> findAllUserInChannel(String channelId) throws CustomException {
    Channel ch = channelRepository.findById(channelId);
    if (ch == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    List<UserDto> result = new ArrayList<>();
    for (String userId : ch.getUserSet()) {
      User user = userRepository.findById(userId);
      if (user == null) {
        throw new CustomException(ErrorCode.USER_NOT_FOUND);
      }
      result.add(UserDto.from(user,
          userStatusService.findById(user.getId().toString()).isOnline()));
    }
    return result;
  }

  @Override
  public boolean addUserToChannel(String channelId, String userId) throws CustomException {
    //todo - set 노출 수정 - how?
    //여기 검사하는 로직 if문으로 판별하도록 전부 수정하기

    try {
      //해당 채널이 DB에 존재하는 채널인지 검사
      Channel c = channelRepository.findById(channelId);
      //해당 유저가 DB에 존재하는 유저인지 검사
      User u = userRepository.findById(userId);
      c.getUserSet().add(u.getId().toString());
      channelRepository.save(c);
      return true;
      //todo - 만약 API 서버라고 가정, 사용자가 요청했을 때 채널이 없어서 API 호출이 실패할텐데 이를 어떻게 알려줄 수 있을지?
    } catch (CustomException e) {
      if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
        System.out.println(
            "Failed to add User to this channel. User with id " + userId + " not found");
      } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
        System.out.println(
            "Failed to add User to this channel. Channel with id " + channelId + " not found");
      }
    }
    return false;
  }

  @Override
  public boolean deleteUserFromChannel(String channelId, String userId) {
    Channel channel = channelRepository.findById(channelId);
    User user = userRepository.findById(userId);
    if (channel.getUserSet().contains(user.getId().toString())) {
      channel.getUserSet().remove(user.getId().toString());
      return true;
    }
    return false;
  }

  @Override
  public boolean isUserInChannel(String channelId, String userId) {
    Channel channel = channelRepository.findById(channelId);
    User user = userRepository.findById(userId);
    return channel.getUserSet().contains(user.getId().toString());
  }

  public Instant getLastMessageTimestamp(Channel channel) throws CustomException {
    //todo
    //이것도 레포지토리 쪽으로 책임 넘기기
    return messageRepository.findAllByChannelId(channel.getId().toString()).stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo).orElse(null);
  }
}
