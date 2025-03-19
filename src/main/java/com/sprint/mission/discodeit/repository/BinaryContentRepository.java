package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<BinaryContent, UUID>는 BinaryContent 엔티티 객체를 다루고, 이 객체의 기본 키가 UUID 타입임을 나타내는 리포지토리
// JpaRepository<BinaryContent, UUID>를 사용하면 BinaryContent 엔티티의 데이터베이스 테이블에 접근하고, UUID 타입의 기본 키로 CRUD 작업을 수행할 수 있음
public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  BinaryContent save(BinaryContent binaryContent);

  Optional<BinaryContent> findById(UUID contentId);

  List<BinaryContent> findAllByIdIn(List<UUID> contentIds);

  boolean existsById(UUID contentId);

  void deleteById(UUID contentId);
}
