package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {


}
