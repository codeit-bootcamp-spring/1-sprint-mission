package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

// TODO 부모 클래스 ChannelMapper에 반드시 기본생성자가 있어야 함-> 자식클래스의 생성자에서는 부모클래스의 기본생성자를 호출하기 때문.
@AllArgsConstructor
@Service
public class BasicChannelService extends ChannelMapper implements ChannelService {

  private ChannelRepository channelRepository;


  @Transactional
  @Override
  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    // 1. 파라미터 예외처리, 변수에 넣기 2. 객체 만들기 3. repository.save
    String channelName = request.getName();
    String channelDescription = request.getDescription();
    Channel channel = Channel.builder()
        .name(channelName)
        .description(channelDescription)
        .type(ChannelType.PUBLIC)
        .build();
    return toDto(channelRepository.save(channel));
  }


  @Transactional
  @Override
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
    Channel channel = Channel.builder()
        .name(null)
        .description(null)
        .type(ChannelType.PRIVATE)
        .build();
    Channel createdChannel = channelRepository.save(channel);
    for (UUID userId : request.getParticipantIds()) {
      ReadStatus readStatus = ReadStatus.builder()
          .channel(createdChannel)
          .lastReadAt(createdChannel.getCreatedAt())
          .build();
      readStatusRepository.save(readStatus);
    }
    return toDto(createdChannel);
  }

  @Transactional(readOnly = true)
  @Override
  // 가장 최근 메세지 정보를 담을 거니까 channelDto 반환.
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(this::toDto)
        .orElseThrow(() -> new ChannelNotFoundException(null));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<Channel> publicChannels = channelRepository.findAll();
    List<ChannelDto> returnChannels = new ArrayList<>();
    for (Channel channel : publicChannels) {
      if (channel.getType().equals(ChannelType.PUBLIC)) {
        returnChannels.add(toDto(channel));
      } else if (channel.getType().equals(ChannelType.PRIVATE)) {
        Optional<ReadStatus> optionalReadStatus = readStatusRepository.findById(channel.getId());
        if (optionalReadStatus.isPresent() && optionalReadStatus.get().getUser().getId()
            .equals(userId)) {
          returnChannels.add(toDto(channel));
        }
      }
    }
    return returnChannels;
  }


  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    // private 채널이면 수정할 수 없다는 말
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(null));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new PrivateChannelUpdateException(null);
    }
    String newName = request.getNewName();
    String newDescription = request.getNewDescription();
    channel.update(newName, newDescription);
    return toDto(
        channel); // TODO 변경 감지(Dirty Checking) : save 안해줘도 JPA가 변경사항을 감지해서 트랜잭션이 끝날 때 자동으로 UPDATE 쿼리 실행
  }


  @Transactional
  @Override
  public void delete(UUID channelId) {
    // 삭제 전에 채널 정보 가져오기 (로그를 위해)
    Optional<Channel> channelToDelete = channelRepository.findById(channelId);
    if (channelToDelete.isEmpty()) {
      throw new ChannelNotFoundException(null);
    }
    // 관련 도메인 데이터 삭제
    readStatusRepository.deleteAllByChannelId(channelId);
    messageRepository.deleteAllByChannelId(channelId);

    // 채널 삭제
    channelRepository.deleteById(channelId);
  }

}
