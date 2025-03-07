package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface BinaryContentRepository extends CrudRepository<BinaryContent, UUID> {

}
