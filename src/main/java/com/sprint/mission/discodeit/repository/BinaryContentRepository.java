package com.sprint.mission.discodeit.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

}
