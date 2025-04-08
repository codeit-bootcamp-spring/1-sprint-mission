package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;

import java.util.List;

public interface ChannelService {

  //생성
  ChannelDto create(CreatePublicChannelDto createPublicChannelDto);

  ChannelDto create(CreatePrivateChannelDTo createPrivateChannelDTo);

  //모두 읽기
  List<ChannelDto> findAllByUserId(String userId);

  //채널 내 메세지 모두 가져오기
  List<MessageDto> findAllMessagesByChannelId(String channelId);

  //읽기
  //단건 조회 - UUID
  ChannelDto findById(String channelId, String userId);

  //수정
  ChannelDto updateChannel(String channelId, UpdateChannelDto updateChannelDto);

  //삭제
  boolean delete(String channelId);

  //미사용 메서드 임시 주석처리
//  boolean addUserToChannel(String channelId, String userId);
//
//  boolean deleteUserFromChannel(String channelId, String userId);
//
//  boolean isUserInChannel(String channelId, String userId);

}
