package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {
//    BinaryContent findById(UUID id);
//    BinaryContent save(MultipartFile file, UUID id);
//    Optional<BinaryContent> findByType_Id(UUID userId);
}
