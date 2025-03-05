package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;

public interface ChannelService {

  //생성
  ChannelResponseDto create(CreatePublicChannelDto createPublicChannelDto);

  ChannelResponseDto create(CreatePrivateChannelDTo createPrivateChannelDTo);

  //모두 읽기
  List<ChannelResponseDto> findAllByUserId(String userId);

  //채널 내 메세지 모두 가져오기
  List<MessageResponseDto> findAllMessagesByChannelId(String channelId);

  //읽기
  //단건 조회 - UUID
  ChannelResponseDto findById(String channelId);

  //다건 조회 - name
  List<ChannelResponseDto> findAllByChannelName(String channelName);

  //다건 조회 - 채널 타입
  List<ChannelResponseDto> findByChannelType(ChannelType channelType);

  //수정
  ChannelResponseDto updateChannel(String channelId, UpdateChannelDto updateChannelDto);

  //삭제
  boolean delete(String channelId);

  List<UserResponseDto> findAllUserInChannel(String channelId);

  boolean addUserToChannel(String channelId, String userId);

  boolean deleteUserFromChannel(String channelId, String userId);

  boolean isUserInChannel(String channelId, String userId);

}
