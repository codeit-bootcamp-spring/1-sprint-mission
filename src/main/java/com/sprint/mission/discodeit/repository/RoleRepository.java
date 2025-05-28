package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {

  Optional<Role> findByName(String name);
}
