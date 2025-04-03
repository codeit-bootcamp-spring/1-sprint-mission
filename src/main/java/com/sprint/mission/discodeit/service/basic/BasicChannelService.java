package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ChannelMapper channelMapper;
  //Service
  private final ReadStatusService readStatusService;

  @Override
  public ChannelDto createPublicChannel(PublicChannelRequest publicChannelRequest) {
    Channel channel = Channel.builder()
        .channelName(publicChannelRequest.name())
        .description(publicChannelRequest.description())
        .type(ChannelType.PUBLIC)
        .build();

    log.debug("DEBUG: Create public channel: {}", channel);
    log.info("Create public channel with ID: {}", channel.getId());
    return channelMapper.toDto(channelRepository.save(channel));
  }

  @Transactional
  @Override
  public ChannelDto createPrivateChannel(PrivateChannelRequest channelCreateDTO) {
    Channel channel = Channel.builder()
        .channelName(channelCreateDTO.getName())
        .description(channelCreateDTO.getDescription())
        .type(ChannelType.PRIVATE)
        .build();

    Channel savedChannel = channelRepository.save(channel);
    createReadStatus(channel, channelCreateDTO);

    log.debug("Create private channel: {}", channel);
    log.info("Create private channel with ID: {}", savedChannel.getId());
    return channelMapper.toDto(savedChannel);
  }


  //ReadStatus서비스에서 ReadStatus를 만드는 함수
  private void createReadStatus(Channel channel, PrivateChannelRequest channelCreateDTO) {
    List<UUID> userIDList = channelCreateDTO.getUserList();
    for (UUID uuid : userIDList) {
      ReadStatusDto readStatusDto = readStatusService.create(
          new ReadStatusRequest(channel.getId(), uuid));
      log.debug("DEBUG: Create read status for user: {} , userStatusId: {}", uuid,
          readStatusDto.id());
      log.info("ReadStatus created with ID: {} ", readStatusDto.id());
    }
  }

  @Override
  public ChannelDto findDTO(UUID uuid) {
    Channel channel = findById(uuid);
    ChannelDto channelDto = channelMapper.toDto(channel);
    return channelDto;
  }

  //특정 User가 볼 수 있는 Channel 목록을 조회
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<ChannelDto> channelDtoList = findAllDTO();
    List<ChannelDto> userChannelDtoList = channelDtoList.stream()
        .filter(channelDto -> channelDto.getType().equals(ChannelType.PUBLIC)
            ||
            (channelDto.getType().equals(ChannelType.PRIVATE) &&
                channelDto.isUserExist(userId)))
        .toList();
    return userChannelDtoList;
  }

  @Override
  public List<ChannelDto> findAllDTO() {
    List<Channel> channelList = findAll();
    List<ChannelDto> channelDtoList = channelList.stream()
        .map(channel -> findDTO(channel.getId()))
        .collect(Collectors.toList());
    return channelDtoList;
  }


  //기존의 read private선언?
  @Override
  public Channel findById(UUID id) {
    return channelRepository.findById(id).orElseThrow(()
        -> new NoSuchElementException("Channel not found"));
  }

  @Override
  public List<Channel> findAll() {
    return channelRepository.findAll();
  }


  //update
  @Override
  public Channel update(ChannelUpdateDTO channelUpdateDTO) {
    if (findDTO(channelUpdateDTO.uuid()).getType() == ChannelType.PRIVATE) {
      log.info("Private channel cannot be updated");
      throw new IllegalArgumentException("PRIVATE  채널은 수정할 수 없습니다.");
    }
    Channel channel = channelRepository.findById(channelUpdateDTO.uuid()).orElseThrow(()
        -> new NoSuchElementException("channel not found"));
    channel.updateName(channelUpdateDTO.name());
    log.debug("DEBUG: Update channel: {}", channel);
    log.info("Update channel with ID: {}", channel.getId());
    return channelRepository.save(channel);
  }

  @Override
  public void deleteChannel(UUID id) {
    channelRepository.deleteById(id);
    log.info("Delete channel with ID: {}", id);
  }
}
