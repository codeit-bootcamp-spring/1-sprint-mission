package com.sprint.mission.repository.binary;

import com.sprint.mission.entity.addOn.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {
}
