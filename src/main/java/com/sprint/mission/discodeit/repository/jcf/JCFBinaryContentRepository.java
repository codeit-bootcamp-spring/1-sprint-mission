package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

public class JCFBinaryContentRepository implements BinaryContentRepository {
	private final Map<UUID, BinaryContent> binaryContentData = new HashMap<>();

	@Override
	public BinaryContent save(BinaryContent binaryContent) {
		binaryContentData.put(binaryContent.getId(), binaryContent);
		return binaryContent;
	}

	@Override
	public Optional<BinaryContent> findById(UUID id) {
		return Optional.ofNullable(binaryContentData.get(id));
	}

	@Override
	public List<BinaryContent> findByMessageId(UUID messageId) {
		List<BinaryContent> result = new ArrayList<>();
		for (BinaryContent content : binaryContentData.values()) {
			if (messageId.equals(content.getMessageId())) {
				result.add(content);
			}
		}
		return result;
	}

	@Override
	public List<BinaryContent> findByAuthorId(UUID authorId) {
		List<BinaryContent> result = new ArrayList<>();
		for (BinaryContent content : binaryContentData.values()) {
			if (authorId.equals(content.getAuthorId())) {
				result.add(content);
			}
		}
		return result;
	}

	@Override
	public void deleteById(UUID id) {
		binaryContentData.remove(id);
	}

	@Override
	public void deleteAllByMessageId(UUID messageId) {
		binaryContentData.entrySet().removeIf(entry -> messageId.equals(entry.getValue().getMessageId()));
	}
}
