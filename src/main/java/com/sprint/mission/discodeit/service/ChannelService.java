package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.channel.*;
import com.sprint.mission.discodeit.entity.user.User;

import java.util.Map;
import java.util.UUID;

public interface ChannelService {

    ChannelResponse createChannel(ChannelCreateRequest request);

    ChannelResponse createPublicChannel(ChannelCreateRequest request, Map<UUID, User> userList);

    ChannelResponse createPrivateChannel(ChannelCreateRequest request, Map<UUID, User> userList);

//    Map<UUID, Channel> getChannelByName(String channelName);

    ChannelFindResponse findChannelById(UUID channelId);

    Map<UUID, ChannelFindResponse> getAllChannels(UUID userId);

    default Map<UUID, ChannelListResponse> getAllChannelsOfUser(String userName) {
        return null;
    }

    ChannelUpdateResponse updateChannel(String channelName, ChannelUpdateRequest request);

    void removeChannelById(UUID channelUUID);

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
