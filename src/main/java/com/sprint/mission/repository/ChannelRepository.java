package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

//    @Query("SELECT c FROM Channel c WHERE c.id = :id")
//    List<Channel> findAll(@Param("userId") UUID userId);

    //List<Channel> findAllByIdIn(List<RE> ids);
}
//    Channel save(Channel channel);
//    Optional<Channel> findById(UUID id) ;
//    List<Channel> findAll();
//    //Channel updateChannelName(Channel updatingChannel);
//
//    void delete(UUID channelId);
//
//    boolean existsById(UUID id);
