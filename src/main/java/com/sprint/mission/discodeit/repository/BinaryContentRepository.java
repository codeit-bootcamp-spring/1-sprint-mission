package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentRepository {
	BinaryContent save(BinaryContent binaryContent);

	Optional<BinaryContent> findById(UUID id);

	List<BinaryContent> findByMessageId(UUID messageId);

	List<BinaryContent> findByAuthorId(UUID authorId);

	void deleteById(UUID id);

	void deleteAllByMessageId(UUID messageId);
}
