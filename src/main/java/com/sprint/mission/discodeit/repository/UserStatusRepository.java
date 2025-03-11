package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserStatusRepository  extends CrudRepository<UserStatus, UUID> {
  @Query("select m from ReadStatus m join fetch m.user u where u.id =: userId")
  Optional<UserStatus> findByUserId(UUID userId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteByUserId(UUID userId);
}
