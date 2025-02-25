package com.sprint.mission.discodeit.message.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.message.entity.BinaryContent;
import com.sprint.mission.discodeit.message.entity.BinaryContentType;

public interface BinaryContentRepository {
	BinaryContent save(BinaryContent binaryContent);

	Optional<BinaryContent> findById(UUID id);

	List<BinaryContent> findByMessageId(UUID messageId);

	List<BinaryContent> findByAuthorId(UUID authorId);

	Optional<BinaryContent> findByAuthorIdAndBinaryContentTypeAndMessageIdIsNull(UUID authorId,
		BinaryContentType binaryContentType);

	void deleteById(UUID id);

	void deleteAllByMessageId(UUID messageId);
}
