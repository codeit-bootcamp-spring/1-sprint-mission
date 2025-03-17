package com.sprint.mission.discodeit.repository.jpa.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentJpaRepository extends JpaRepository<BinaryContent, UUID> {

}
