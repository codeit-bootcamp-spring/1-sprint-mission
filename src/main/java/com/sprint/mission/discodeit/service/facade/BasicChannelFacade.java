package com.sprint.mission.discodeit.service.facade;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BasicChannelFacade implements ChannelFacade{

  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final MessageService messageService;
  private final ChannelMapper channelMapper;
  private final EntityValidator validator;

  @Override
  public PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {

    channelDto.userIds().forEach(
        id -> validator.findOrThrow(User.class, id, new UserNotFoundException())
    );

    Channel channel = channelService.createPrivateChannel(channelMapper.toEntity(channelDto));

    readStatusService.createMultipleReadStatus(channelDto.userIds(), channel.getUUID());

    return channelMapper.toPrivateDto(channel);
  }

  @Override
  public PublicChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {
    Channel channel = channelService.createPublicChannel(channelMapper.toEntity(channelDto));
    return channelMapper.toPublicDto(channel);
  }

  @Override
  public FindChannelResponseDto getChannelById(String channelId) {
    Channel channel = channelService.getChannelById(channelId);
    List<String> userIds = channel.getParticipatingUsers();
    Instant lastMessageTime = messageService.getLatestMessageByChannel(channelId).getCreatedAt();
    return channelMapper.toFindChannelDto(channel, lastMessageTime, userIds);
  }

  @Override
  public List<FindChannelResponseDto> findAllChannelsByUserId(String userId) {
    // user 에게 할당된 모든 ReadStatus
    List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
    // user 가 조회할 수 있는 모든 Channel
    List<Channel> channels = channelService.findAllChannelsByUserId(userId, statuses);


    Map<String, Instant> latestMessagesByChannel = messageService.getLatestMessageForChannels(channels);
    Map<String, List<String>> channelReadStatuses = readStatusService.getUserIdsForChannelReadStatuses(channels);

    return channels.stream()
        .map(channel -> channelMapper.toFindChannelDto(
            channel,
            latestMessagesByChannel.getOrDefault(channel.getUUID(), Instant.EPOCH),
            channelReadStatuses.getOrDefault(channel.getUUID(), List.of())
        )).toList();
  }

  @Override
  public FindChannelResponseDto updateChannel(ChannelUpdateDto channelUpdateDto) {
    Channel channel = channelService.updateChannel(channelUpdateDto.getChannelId(), channelUpdateDto.getChannelName(), channelUpdateDto.getMaxNumberOfPeople());
    Instant lastMessageTime = messageService.getLatestMessageByChannel(channel.getUUID()).getCreatedAt();
    List<String> userIds = channel.getParticipatingUsers();
    return channelMapper.toFindChannelDto(
        channel,
        lastMessageTime,
        userIds
    );

  }

  @Override
  public void deleteChannel(String channelId){
    channelService.deleteChannel(channelId);
    readStatusService.deleteByChannel(channelId);
    //TODO : 한번에 삭제 메서드 생성
    messageService.getMessagesByChannel(channelId).stream()
        .forEach(message -> messageService.deleteMessage(message.getUUID()));
  }
}
