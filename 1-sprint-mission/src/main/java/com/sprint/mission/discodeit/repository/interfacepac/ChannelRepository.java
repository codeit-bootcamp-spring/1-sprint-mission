package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ChannelRepository {
    Channel save(Channel channel);
    //
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    List<Channel> findAllByType(ChannelType type);              // 채널 타입으로만 찾는법
    List<Channel> findAllByOwnerAndType(User owner, ChannelType type);          // 소유자랑 채널 타입으로 찾는거
    List<Channel> findAllByUserId(UUID userId);                     // 유저 아이디로 찾는법
    //
    boolean existsByUser(User user);
    //
    void deleteByChannel(Channel channel);


}
