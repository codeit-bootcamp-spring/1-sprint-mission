package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  BinaryContent save(BinaryContent binaryContent);

  void deleteById(UUID id);

  Optional<BinaryContent> findById(UUID id);

  List<BinaryContent> findAll();

  // 존재 검증
  boolean existsById(UUID id);
}
