package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {

    //채널에 대한 CURD.

    void createChannel(Channel channel);

    Channel readChannel(String id);

    List<Channel> readAllChannel();
    void modifyChannel(String id, String name); //채널 네임 변경
    public void deleteChannel (String id);

    public void addUser(Channel channel, User user); // 유저 추가

}
