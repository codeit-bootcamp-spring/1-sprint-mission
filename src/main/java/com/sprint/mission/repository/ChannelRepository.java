package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID>, CustomChannelRepository {

    List<Channel> findAllByIdIn(List<UUID> ids);


    List<Channel> findAllByChannelType(ChannelType channelType);
}
//    Channel save(Channel channel);
//    Optional<Channel> findById(UUID id) ;
//    List<Channel> findAll();
//    //Channel updateChannelName(Channel updatingChannel);
//
//    void delete(UUID channelId);
//
//    boolean existsById(UUID id);
