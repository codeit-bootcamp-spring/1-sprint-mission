package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    ReadStatus save(ReadStatusDto readStatus);

    Optional<ReadStatus> findById(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);


    boolean existsById(UUID id);

    void deleteById(UUID id);

}
