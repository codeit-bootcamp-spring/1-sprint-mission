package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByUser(User user);

  List<ReadStatus> findAllByChannel(Channel channel);

  Optional<ReadStatus> findByUserAndChannel(User user, Channel channel);

  List<ReadStatus> findAllByUser_Id(UUID userId);

}
