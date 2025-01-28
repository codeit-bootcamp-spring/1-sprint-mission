package com.sprint.misson.discordeit.repository;

import com.sprint.misson.discordeit.entity.Channel;

import java.util.List;

public interface ChannelRepository {

    //저장
    Channel save(Channel channel);

    //delete
    boolean delete(Channel channel);

    //search
    Channel findById(String id);

    List<Channel> findAll();

}
