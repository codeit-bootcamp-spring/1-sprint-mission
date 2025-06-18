package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.entity.BinaryContent;
import com.spirnt.mission.discodeit.entity.BinaryContentUploadStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

    @Modifying
    @Query("UPDATE BinaryContent b SET b.uploadStatus = :status WHERE b.id = :id")
    void updateUploadStatus(@Param("id") UUID id,
        @Param("status") BinaryContentUploadStatus status);

}
