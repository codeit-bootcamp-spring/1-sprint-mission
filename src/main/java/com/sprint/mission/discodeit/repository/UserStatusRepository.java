package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserStatusRepository  extends JpaRepository<UserStatus, UUID> {

  @Query("select m from ReadStatus m join fetch m.user u where u.id =: userId")
  Optional<UserStatus> findByUserId(@Param("userId") UUID userId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteByUserId(UUID userId);
}
