package com.sprint.mission.service.jcf.main;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.FindChannelAllDto;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageService messageService;

  @Override
  public Channel createPublicChannel(PublicChannelCreateDTO request) {
    return channelRepository.save(request.toChannel());
  }

  @Override
  public Channel createPrivateChannel(PrivateChannelCreateDTO request) {
    Channel createdChannel = channelRepository.save(request.toChannel());
    request.participantIds().stream()
        .map(userId -> new ReadStatus(userId, createdChannel.getId(), Instant.MIN))
        .forEach(readStatusRepository::save);
    return createdChannel;
  }


  /**
   * [ ] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId [ ] PUBLIC 채널
   * 목록은 전체 조회합니다. [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
   */
  @Override
  public Channel findById(UUID channelId) {
    return channelRepository.findById(channelId)
        .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));
  }

  // 카피
  @Override
  public List<FindChannelAllDto> findAllByUserId(UUID userId) {

    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getChannelType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  @Override
  public List<Channel> findAll() {
    return channelRepository.findAll();
  }

  @Override
  public void update(UUID channelId, ChannelDtoForRequest dto) {
    Channel updatingChannel = channelRepository.findById(channelId)
        .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));

    if (updatingChannel.getChannelType().equals(ChannelType.PRIVATE)){
      throw new CustomException(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL);
    }

    updatingChannel.updateByDTO(dto);
    channelRepository.save(updatingChannel);
  }


  @Override
  public void delete(UUID channelId) {
    Channel deletingChannel = channelRepository.findById(channelId)
        .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));

    if (deletingChannel.getChannelType().equals(ChannelType.PRIVATE)){
      readStatusRepository.deleteAllByChannelId(channelId);
    }
    messageService.deleteAllByChannelId(channelId);
    channelRepository.delete(channelId);
  }

  /**
   * 중복 검증
   */
  public void validateDuplicateName(String name) {
    boolean isDuplicate = channelRepository.findAll().stream()
        .anyMatch(channel -> channel.getName().equals(name));
    if (isDuplicate) {
      throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);
    }
  }

  /**
   * 응답 DTO (타입별)
   */
  private FindChannelDto getFindChannelDto(Channel findedChannel) {
    return (findedChannel.getChannelType().equals(ChannelType.PRIVATE)
        ? new FindPrivateChannelDto(findedChannel)
        : new FindPublicChannelDto(findedChannel));
  }

  // 카피 해온거
  private FindChannelAllDto toDto(Channel channel) {
    Instant lastMessageAt = messageService.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreateAt).reversed())
        .map(Message::getCreateAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUserId)
          .forEach(participantIds::add);
    }

    return new FindChannelAllDto(
        channel.getId(),
        channel.getChannelType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }

//        public Map<FindUserDto, Instant> lastReadTimeListInChannel(UUID channelId) {
//            Channel inChannel = channelRepository.findById(channelId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));
//            if (inChannel.getChannelType().equals(ChannelType.PUBLIC)) {
//                throw new CustomException(ErrorCode.CANNOT_REQUEST_LAST_READ_TIME);
//            }
//
//            // 유저별 이 채널 마지막 readTime
//            // 이 때 쓰는 findUserDto는 profile null
//            Map<FindUserDto, Instant> readTimeMap = new HashMap<>();
//            for (User user : inChannel.getUserList()) {
//                UserStatus status = userStatusService.findById(user.getId());
//                readTimeMap.put(new FindUserDto(user, status.isOnline()), user.getReadStatus().findLastReadByChannel(channelId));
//            }
//            return readTimeMap;
//        }
}

