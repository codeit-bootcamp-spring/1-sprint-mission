package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.ChannelValidationException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.ChannelConstant.CHANNEL_NAME_MAX_LENGTH;
import static com.sprint.mission.discodeit.constant.ChannelConstant.CHANNEL_NAME_MIN_LENGTH;

public class BasicChannelService implements ChannelService {

  private static volatile BasicChannelService instance;
  private final ChannelRepository channelRepository;

  private BasicChannelService(ChannelRepository channelRepository) {
    this.channelRepository = channelRepository;
  }

  public static BasicChannelService getInstance(ChannelRepository channelRepository) {
    if (instance == null) {
      synchronized (BasicChannelService.class) {
        if (instance == null) {
          instance = new BasicChannelService(channelRepository);
        }
      }
    }
    return instance;
  }

  @Override
  public BaseChannel createChannel(BaseChannel channel) {
    //TODO: exception tuning, other validations
    if(!validChannelName(channel.getChannelName())) throw new ChannelValidationException();
    channelRepository.save(channel);
    return channel;
  }

  private boolean validChannelName(String channelName){
    if(channelName.length() < CHANNEL_NAME_MIN_LENGTH || channelName.length() > CHANNEL_NAME_MAX_LENGTH) return false;
    return true;
  }

  @Override
  public Optional<BaseChannel> getChannelById(String channelId) {
    Optional<BaseChannel> channel = channelRepository.findById(channelId);
    if(channel.isEmpty()) throw new ChannelNotFoundException();
    //TODO: Optional 삭제
    return channel;
  }

  @Override
  public List<BaseChannel> getAllChannels() {
    return channelRepository.findAll();
  }

  @Override
  public List<BaseChannel> getChannelsByCategory(String categoryId) {
    return null;
  }

  @Override
  public void updateChannel(String channelId, ChannelUpdateDto updatedChannel) {
    Optional<BaseChannel> channel = channelRepository.findById(channelId);

    if(channel.isEmpty()) throw new ChannelNotFoundException();
    BaseChannel originalChannel = channel.get();
    updatedChannel.getChannelName().ifPresent(name -> {
      validChannelName(name);
      originalChannel.setChannelName(name);
    });

    updatedChannel.getTag().ifPresent(originalChannel::setTag);
    updatedChannel.getMaxNumberOfPeople().ifPresent(originalChannel::setMaxNumberOfPeople);
    updatedChannel.getIsPrivate().ifPresent(originalChannel::setIsPrivate);

    channelRepository.update(originalChannel);
  }

  @Override
  public void deleteChannel(String channelId) {
    if(channelRepository.findById(channelId).isEmpty()) throw new ChannelNotFoundException();
    channelRepository.delete(channelId);
  }

  @Override
  public String generateInviteCode(BaseChannel channel) {
    return null;
  }

  @Override
  public void setPrivate(BaseChannel channel) {

  }

  @Override
  public void setPublic(BaseChannel channel) {

  }
}
