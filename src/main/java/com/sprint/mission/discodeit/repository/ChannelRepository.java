package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelRepository {

  //저장
  Channel save(Channel channel);

  //delete
  boolean delete(String channelId);

  //search
  Channel findById(String id);


  List<Channel> findAll();

}
