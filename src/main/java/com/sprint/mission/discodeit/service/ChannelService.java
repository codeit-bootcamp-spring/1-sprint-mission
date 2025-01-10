package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;
/*
*  CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 구현한 인터페이스
* */
public interface ChannelService {
    void addChannel(Channel channel);
    Channel getChannel(UUID id);
    List<Channel> getAllChannels();
    void updateChannel(UUID id, String newName);
    void deleteChannel(UUID id);
}
