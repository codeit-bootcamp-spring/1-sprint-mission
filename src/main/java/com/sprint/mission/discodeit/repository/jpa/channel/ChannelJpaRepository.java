package com.sprint.mission.discodeit.repository.jpa.channel;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelJpaRepository extends JpaRepository<Channel, UUID> {

}
