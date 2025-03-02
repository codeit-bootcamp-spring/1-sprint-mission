package com.sprint.mission.discodeit.binaryContent.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.binaryContent.entity.BinaryContent;

public interface BinaryContentRepository {
	BinaryContent save(BinaryContent binaryContent);

	Optional<BinaryContent> findById(UUID id);

	List<BinaryContent> findAllByIdIn(List<UUID> ids);

	boolean existById(UUID id);

	void deleteById(UUID id);
}
