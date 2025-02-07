package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//사용자가 속한 PRIVATE 채널을 조회
//특정 PRIVATE 채널에 속한 사용자 ID 목록을 조회
@Repository
public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    //
    List<ReadStatus> findAllByChannel(Channel channel);
    List<ReadStatus> findAllByUser(User user);
    List<Channel>findChannelsByUser(User Owner);
    List<UUID>findUserIdsByChannel(Channel channel);
    Optional<ReadStatus> findById(UUID id);
    //
     boolean existsByUserAndChannel(User user, Channel channel);
    //
    void deleteByChannel(Channel channel);
    void deleteById(UUID id);
}
