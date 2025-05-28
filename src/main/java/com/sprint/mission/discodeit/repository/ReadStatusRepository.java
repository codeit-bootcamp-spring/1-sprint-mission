package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ReadStatusRepository extends CrudRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByOwner(User owner);

  List<ReadStatus> findAllByOwnerAndChannel(User owner, Channel channel);
}
