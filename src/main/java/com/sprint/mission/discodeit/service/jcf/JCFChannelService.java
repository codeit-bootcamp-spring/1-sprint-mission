package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

  private static volatile JCFChannelService channelRepository;
  private final Map<String, BaseChannel> data;

  private JCFChannelService() {
    data = new HashMap<>();
  }

  public static JCFChannelService getInstance() {
    if (channelRepository == null) {
      synchronized (JCFChannelService.class) {
        if (channelRepository == null) {
          channelRepository = new JCFChannelService();
        }
      }
    }
    return channelRepository;
  }

  @Override
  public BaseChannel createChannel(BaseChannel channel) {
    if (!checkIfChannelNameIsEmpty(channel.getChannelName())) {
      throw new IllegalArgumentException("채널명은 비어있을 수 없습니다.");
    }
    data.put(channel.getUUID(), channel);
    return channel;
  }

  @Override
  public Optional<BaseChannel> getChannelById(String channelId) {
    return Optional.ofNullable(data.get(channelId));
  }

  @Override
  public List<BaseChannel> getAllChannels() {
    return Collections.unmodifiableList(new ArrayList<>(data.values()));
  }

  @Override
  public List<BaseChannel> getChannelsByCategory(String categoryId) {
    return null;
  }

  @Override
  public void updateChannel(String channelId, ChannelUpdateDto updatedChannel) {
    BaseChannel channel = data.get(channelId);
    //TODO: user 권한 확인
    //TODO: channel 존재하지 않을때

    synchronized (channel) {
      updatedChannel.getChannelName().ifPresent(channel::setChannelName);
      updatedChannel.getMaxNumberOfPeople().ifPresent(channel::setMaxNumberOfPeople);
      updatedChannel.getTag().ifPresent(channel::setTag);
      updatedChannel.getIsPrivate().ifPresent(aPrivate -> channel.updatePrivate(aPrivate));
    }
  }

  @Override
  public void deleteChannel(String channelId) {
    //TODO : 소유자 확인 로직
    data.remove(channelId);
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

  private boolean checkIfChannelNameIsEmpty(String channelName) {
    return !channelName.isEmpty();
  }

  private boolean checkIfUserIsOwner(BaseChannel channel, User user) {
    return true;
  }
}
