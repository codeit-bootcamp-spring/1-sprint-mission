package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    List<FileEntity> findByIdIn(List<UUID> ids);
}
