//package com.sprint.mission.discodeit.repository;
//
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public interface ReadStatusRepository {
//
//  ReadStatus save(ReadStatus readStatus);
//
//  Optional<ReadStatus> findById(UUID id);
//
//  List<ReadStatus> findAllByUserId(UUID userId);
//
//  List<ReadStatus> findAllByChannelId(UUID channelId);
//
//  boolean existsById(UUID id);
//
//  void deleteById(UUID id);
//
//  void deleteAllByChannelId(UUID channelId);
//}
package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  void deleteAllByChannelId(UUID channelId);
}