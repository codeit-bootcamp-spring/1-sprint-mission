package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String channelName, User owner, Map<UUID, User> userList);

    Map<UUID, Channel> getChannelByName(String channelName);

    Channel findChannelById(UUID channelId);

    Map<UUID, Channel> getAllChannels();

    Channel updateChannel(UUID channelUUID, String channelName, User changeUser);

    void removeChannelById(UUID channelUUID);

    void addUserChannel(UUID channelUUID, User addUser);

    void kickUserChannel(UUID channelUUID, User kickUser);

    // 채널에 메세지 추가
    void addMessageInCh(UUID channelId, Message message);

    // 채널에 있는 메세지 삭제
    void removeMessageInCh(UUID channelId, Message removeMessage);

    // 채널 메세지 조회
    Message findChannelMessageById(UUID channelId, UUID messageId);

    // 채널 메세지 모든 조회
    Map<UUID, Message> findChannelInMessageAll(UUID channelId);
}
