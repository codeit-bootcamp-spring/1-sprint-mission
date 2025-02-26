/*
package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

  private static volatile JCFChannelService channelRepository;
  private final Map<String, Channel> data;

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
  public Channel createChannel(Channel channel) {
    if (!checkIfChannelNameIsEmpty(channel.getChannelName())) {
      throw new IllegalArgumentException("채널명은 비어있을 수 없습니다.");
    }
    data.put(channel.getId(), channel);
    return channel;
  }

  @Override
  public FindChannelResponseDto getChannelById(String channelId) {
    return Optional.ofNullable(data.get(channelId));
  }

  @Override
  public List<FindChannelResponseDto> findAllChannelsByUserId(String userId) {
    return Collections.unmodifiableList(new ArrayList<>(data.values()));
  }

  @Override
  public List<Channel> getChannelsByCategory(String categoryId) {
    return null;
  }

  @Override
  public void updateChannel(ChannelUpdateDto updatedChannel) {
    Channel channel = data.get(channelId);
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
  public String generateInviteCode(Channel channel) {
    return null;
  }

  @Override
  public void setPrivate(Channel channel) {

  }

  @Override
  public void setPublic(Channel channel) {

  }

  private boolean checkIfChannelNameIsEmpty(String channelName) {
    return !channelName.isEmpty();
  }

  private boolean checkIfUserIsOwner(Channel channel, User user) {
    return true;
  }
}
*/
