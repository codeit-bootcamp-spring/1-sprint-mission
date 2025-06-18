package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContent.UploadStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update BinaryContent b set b.uploadStatus = :status where b.id = :id")
  void updateStatus(@Param("id") UUID id, @Param("status") UploadStatus status);

}
