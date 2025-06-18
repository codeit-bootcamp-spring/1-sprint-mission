package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  List<BinaryContent> findAllByIdIn(List<UUID> ids);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("UPDATE BinaryContent b SET b.uploadStatus = :status WHERE b.id = :id")
  void updateUploadStatus(@Param("id") UUID id, @Param("status") BinaryContentUploadStatus status);
}
