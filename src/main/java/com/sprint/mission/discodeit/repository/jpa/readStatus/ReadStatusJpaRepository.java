package com.sprint.mission.discodeit.repository.jpa.readStatus;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusJpaRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByUser(User user);

  List<ReadStatus> findAllByChannel(Channel channel);
}
