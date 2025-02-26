package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.channel.ChannelListResponse;
import com.sprint.mission.discodeit.entity.channel.ChannelResponse;
import com.sprint.mission.discodeit.entity.channel.add.ChannelAddMessageRequest;
import com.sprint.mission.discodeit.entity.channel.add.ChannelAddUserResponse;
import com.sprint.mission.discodeit.entity.channel.create.ChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateResponse;
import com.sprint.mission.discodeit.entity.channel.find.ChannelFindOfUserResponse;
import com.sprint.mission.discodeit.entity.channel.find.ChannelFindResponse;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyResponse;
import com.sprint.mission.discodeit.entity.channel.update.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelUpdateResponse;
import com.sprint.mission.discodeit.entity.user.User;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {

  ChannelResponse createChannel(ChannelCreateRequest request);

  ChannelResponse createPublicChannel(ChannelCreateRequest request, Map<UUID, User> userList);

  PublicChannelCreateResponse createPublicChannel(PublicChannelCreateRequest request);

  ChannelResponse createPrivateChannel(ChannelCreateRequest request, Map<UUID, User> userList);

  PrivateChannelCreateResponse createPrivateChannel(PrivateChannelCreateRequest request);

//    Map<UUID, Channel> getChannelByName(String channelName);

  ChannelFindResponse findChannelById(UUID channelId);

  Map<UUID, ChannelFindResponse> getAllChannels(UUID userId);

  default Map<UUID, ChannelListResponse> getAllChannelsOfUser(String userName) {
    return null;
  }

  List<ChannelFindOfUserResponse> findAllChannelsFindByUserId(UUID userId);

  ChannelUpdateResponse updateChannel(String channelName, ChannelUpdateRequest request);

  ChannelModifyResponse modifyChannel(UUID channelId, ChannelModifyRequest request);

  UUID removeChannelById(UUID channelUUID);

  default UUID removeChannelByName(String channelName) {
    return null;
  }

  ChannelAddUserResponse addUserChannel(UUID channelUUID, String username);

  //
//    void kickUserChannel(UUID channelUUID, User kickUser);
//
//    // 채널에 메세지 추가
  void sendMessage(ChannelAddMessageRequest request);
//
//    // 채널에 있는 메세지 삭제
  //void removeMessageInCh(UUID channelId, Message removeMessage);
//
//    // 채널 메세지 조회
//    Message findChannelMessageById(UUID channelId, UUID messageId);
//
//    // 채널 메세지 모든 조회
//    Map<UUID, Message> findChannelInMessageAll(UUID channelId);
}
