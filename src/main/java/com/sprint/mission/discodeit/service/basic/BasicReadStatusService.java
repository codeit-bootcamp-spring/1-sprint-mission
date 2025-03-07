package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;

  @Override
  public ReadStatusDto create(CreateReadStatusDto createReadStatusDto)
      throws CustomException {

    if (createReadStatusDto == null || createReadStatusDto.channelId() == null
        || createReadStatusDto.userId() == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }
    Channel channel = channelRepository.findById(UUID.fromString(createReadStatusDto.channelId()))
        .orElse(null);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    User user = userRepository.findById(UUID.fromString(createReadStatusDto.userId())).orElse(null);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
        UUID.fromString(createReadStatusDto.channelId()),
        UUID.fromString(createReadStatusDto.userId())).orElse(null);

    if (readStatus != null) {
      throw new CustomException(ErrorCode.READ_STATUS_ALREADY_EXIST);
    }

    readStatus = new ReadStatus(channel, user, createReadStatusDto.lastReadAt());
    ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

    return ReadStatusDto.from(savedReadStatus, isNewMessage(readStatus));
  }

  @Override
  public List<ReadStatusDto> createByChannelId(String channelId) throws CustomException {
    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElse(null);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    //todo - 새로들어온 유저만 해당 채널의 수신 정보를 생성하는 일이 있을까?
    if (!findAllByChannelId(channelId).isEmpty()) {
      throw new CustomException(ErrorCode.READ_STATUS_ALREADY_EXIST);
    }

    //todo - 리팩토링
    List<UserDto> userDtos = List.of();

    //channel에 set이 필요없는게, readstatus 조회하면 됨... n:1 1:n관계로 쪼갠게 readStatus니까
    //이거 수정하면 한세월 걸릴것 같은데...
    //일단해보자
    //private이면 미리 생성하고
    //public이면 채널 id로 생성하도록?
    if (channel.getChannelType() == ChannelType.PUBLIC) {
      userDtos = .findAll().stream().toList();
    } else if (channel.getChannelType() == ChannelType.PRIVATE) {
      userDtos = channel.getUserSet().stream().toList();
    }

    List<ReadStatusDto> readStatusDtos = new ArrayList<>();

    for (UserDto userDto : userDtos) {
      ReadStatus readStatus = new ReadStatus(channel, user, Instant.now());
      ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

      readStatusDtos.add(
          ReadStatusDto.from(savedReadStatus, isNewMessage(readStatus)));
    }

    return readStatusDtos;
  }

  @Override
  public ReadStatusDto findById(String readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId);
    return ReadStatusDto.from(readStatus, isNewMessage(readStatus));
  }

  @Override
  public ReadStatusDto update(String readStatusId,
      UpdateReadStatusDto updateReadStatusDto) {

    if (updateReadStatusDto == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    ReadStatus readStatus = readStatusRepository.findById(readStatusId);
    if (readStatus == null) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    readStatus.setLastReadAt(updateReadStatusDto.newLastReadAt());
    readStatus.setUpdatedAt(updateReadStatusDto.newLastReadAt());

    return ReadStatusDto.from(readStatus, isNewMessage(readStatus));
  }

  @Override
  public List<ReadStatusDto> updateByUserId(String userId,
      UpdateReadStatusDto updateReadStatusDto) {

    List<ReadStatus> readStatuses = readStatusRepository.findByUserId(userId);

    if (readStatuses == null || readStatuses.isEmpty()) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    List<ReadStatusDto> readStatusDtos = new ArrayList<>();

    //기능 미사용으로 임시 주석처리
//    for (ReadStatus readStatus : readStatuses) {
//      if (readStatus.isUpdated(updateReadStatusDto)) {
//        readStatusResponseDtos.add(ReadStatusResponseDto.from(readStatusRepository.save(readStatus),
//            isNewMessage(readStatus)));
//      } else {
//        readStatusResponseDtos.add(
//            ReadStatusResponseDto.from(readStatus, isNewMessage(readStatus)));
//      }
//    }
    return readStatusDtos;
  }

  @Override
  public List<ReadStatusDto> updateByChannelId(String channelId,
      UpdateReadStatusDto updateReadStatusDto) {

    Channel channel = channelRepository.findById(channelId);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channelId);
    if (readStatuses == null || readStatuses.isEmpty()) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    List<ReadStatusDto> readStatusDtos = new ArrayList<>();
    for (ReadStatus readStatus : readStatuses) {
      readStatus.setUpdatedAt(updateReadStatusDto.newLastReadAt());
      readStatusDtos.add(ReadStatusDto.from(readStatusRepository.save(readStatus),
          isNewMessage(readStatus)));
    }
    return readStatusDtos;
  }

  public List<ReadStatusDto> findAllByUserId(String userId) {

    List<ReadStatus> allReadStatusByUserId = readStatusRepository.findByUserId(userId);

    if (allReadStatusByUserId == null) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    List<ReadStatusDto> readStatusDtos = new ArrayList<>();

    for (ReadStatus readStatus : allReadStatusByUserId) {
      readStatusDtos.add(ReadStatusDto.from(readStatus, isNewMessage(readStatus)));
    }
    return readStatusDtos;
  }

  @Override
  public List<ReadStatusDto> findAllByChannelId(String channelId) {
    return readStatusRepository.findByChannelId(channelId).stream()
        .map(r -> ReadStatusDto.from(r, isNewMessage(r))).toList();
  }

  @Override
  public boolean delete(String readStatusId) {
    return readStatusRepository.delete(readStatusId);
  }

  public boolean isNewMessage(ReadStatus readStatus) throws CustomException {
    Instant lastMessageTimestamp = messageRepository.findAllByChannelId(
            readStatus.getChannel().getId().toString())
        .stream().map(Message::getCreatedAt).max(Instant::compareTo).orElse(null);

    if (lastMessageTimestamp == null) {
      return false;
    }
    return lastMessageTimestamp.isAfter(readStatus.getLastReadAt());
  }

}
