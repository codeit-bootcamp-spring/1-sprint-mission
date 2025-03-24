package com.sprint.mission.repository;

import com.sprint.mission.entity.addOn.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinarycontentRepository extends JpaRepository<BinaryContent, UUID> {
    BinaryContent save(BinaryContent binaryContent);

    Optional<BinaryContent> findById(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> idList);

    void delete(UUID id);

    boolean existsById(UUID id);
}
