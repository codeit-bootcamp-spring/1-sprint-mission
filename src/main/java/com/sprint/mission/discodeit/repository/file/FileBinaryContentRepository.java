package com.sprint.mission.discodeit.repository.file;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
	@Override
	public BinaryContent save(BinaryContent binaryContent) {
		return null;
	}

	@Override
	public Optional<BinaryContent> findById(UUID id) {
		return Optional.empty();
	}

	@Override
	public List<BinaryContent> findByAuthorId(UUID authorId) {
		return List.of();
	}

	@Override
	public Optional<BinaryContent> findByMessageId(UUID messageId) {
		return Optional.empty();
	}

	@Override
	public List<BinaryContent> findAllByAuthorId(UUID authorId) {
		return List.of();
	}

	@Override
	public void deleteById(UUID id) {

	}
}
