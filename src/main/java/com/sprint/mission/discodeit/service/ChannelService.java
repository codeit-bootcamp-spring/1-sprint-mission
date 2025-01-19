package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface ChannelService {
    HashMap<UUID, Channel> getChannelsMap();

    UUID createChannel(String channelName);

    Channel getChannel(UUID channelId);

    boolean deleteChannel(UUID channelId);

    boolean isChannelExist(UUID channelId);

    boolean changeChannelName(UUID channelId, String newName);

    boolean changeChannelMembers(UUID channelId, ArrayList<User> members);

    boolean addChannelMember(UUID channelID, UUID memberID);

    //채널에 속한 멤버 관리하는 메서드 추가
}

