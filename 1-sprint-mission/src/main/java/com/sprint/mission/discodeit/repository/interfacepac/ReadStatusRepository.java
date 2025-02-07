package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//사용자가 속한 PRIVATE 채널을 조회
//특정 PRIVATE 채널에 속한 사용자 ID 목록을 조회
@Repository
public interface ReadStatusRepository {

    void save(ReadStatus readStatus);

    List<ReadStatus> findAllByChannel(Channel channel);
    List<Channel>findChannelsByUser(User Owner);
    List<UUID>findUserIdsByChannel(Channel channel);

    void deleteByChannel(Channel channel);
}
