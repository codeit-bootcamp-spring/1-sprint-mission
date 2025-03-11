package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelRequestDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  //Service
  private final ReadStatusService readStatusService;

  @Override
  public Channel createPublicChannel(ChannelCreateDTO channelCreateDTO) {
    Channel channel = Channel.builder()
        .channelName(channelCreateDTO.name())
        .description(channelCreateDTO.description())
        .type(ChannelType.PUBLIC)
        .build();

    return channelRepository.save(channel);
  }

  @Override
  public Channel createPrivateChannel(PrivateChannelCreateDTO channelCreateDTO) {
    Channel channel = Channel.builder()
        .channelName(channelCreateDTO.getName())
        .description(channelCreateDTO.getDescription())
        .type(ChannelType.PUBLIC)
        .build();

    createReadStatus(channel, channelCreateDTO);

    return channelRepository.save(channel);
  }


  //ReadStatus서비스에서 ReadStatus를 만드는 함수
  private void createReadStatus(Channel channel, PrivateChannelCreateDTO channelCreateDTO) {
    List<UUID> userIDList = channelCreateDTO.getUserList();
    for (UUID uuid : userIDList) {
      System.out.println("ReadStatus created");
      readStatusService.create(new ReadStatusCreateDTO(channel.getId(), uuid));
    }
  }

  @Override
  public ChannelRequestDTO findDTO(UUID uuid) {
    Channel channel = findById(uuid);

    //Public 일 때 userIdList , time은 null
    //이 아이디리스트는 readStatus에서 찾아야함.
    List<UUID> userIdList = null;
    Instant time = null;
    if (channel.getType() == ChannelType.PRIVATE) {
      userIdList = readStatusRepository.findAllUserIdByChannelId(uuid);
      time = readStatusRepository.findLatestTimeByChannelId(uuid);
    }
    ChannelRequestDTO channelRequestDTO = new ChannelRequestDTO(channel, time, userIdList);
    return channelRequestDTO;
  }

  //특정 User가 볼 수 있는 Channel 목록을 조회
  @Override
  public List<ChannelRequestDTO> findAllByUserId(UUID userId) {
    List<ChannelRequestDTO> channelRequestDTOList = findAllDTO();
    List<ChannelRequestDTO> userChannelRequestDTOList = channelRequestDTOList.stream()
        .filter(channelRequestDTO -> channelRequestDTO.getType().equals(ChannelType.PUBLIC)
            ||
            (channelRequestDTO.getType().equals(ChannelType.PRIVATE) &&
                channelRequestDTO.isUserExist(userId)))
        .toList();
    return userChannelRequestDTOList;
  }

  @Override
  public List<ChannelRequestDTO> findAllDTO() {
    List<Channel> channelList = findAll();
    List<ChannelRequestDTO> channelRequestDTOList = channelList.stream()
        .map(channel -> findDTO(channel.getId()))
        .collect(Collectors.toList());
    return channelRequestDTOList;
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
      throw new IllegalArgumentException("PRIVATE  채널은 수정할 수 없습니다.");
    }
    Channel channel = channelRepository.findById(channelUpdateDTO.uuid()).orElseThrow(()
        -> new NoSuchElementException("channel not found"));
    channel.updateName(channelUpdateDTO.name());
    return channelRepository.save(channel);
  }

  @Override
  public void deleteChannel(UUID id) {
    //관련된 도메인 삭제
    messageRepository.deleteByChannelId(id);
    Channel channel = findById(id);
    if (channel.getType() == ChannelType.PRIVATE) {
      readStatusService.deleteByChannelId(id);
    }
    channelRepository.deleteById(id);
  }
}
