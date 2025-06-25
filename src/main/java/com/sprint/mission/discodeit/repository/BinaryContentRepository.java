package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  @Transactional
  @Modifying
  @Query("UPDATE BinaryContent bc SET bc.uploadStatus = :status WHERE bc.id = :id")
  void updateUploadStatus(@Param("id") UUID id, @Param("status") BinaryContentUploadStatus status);
}
