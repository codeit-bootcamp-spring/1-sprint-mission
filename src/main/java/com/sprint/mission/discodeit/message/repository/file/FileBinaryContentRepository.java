package com.sprint.mission.discodeit.message.repository.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.global.util.FileStorage;
import com.sprint.mission.discodeit.global.util.JsonFileStorage;
import com.sprint.mission.discodeit.message.entity.BinaryContent;
import com.sprint.mission.discodeit.message.entity.BinaryContentType;
import com.sprint.mission.discodeit.message.repository.BinaryContentRepository;

public class FileBinaryContentRepository implements BinaryContentRepository {
	private final Path rootDir;
	private static final String BINARYCONTENT_FILE = "binarycontent.json";
	private final FileStorage<BinaryContent> fileStorage;

	public FileBinaryContentRepository(String fileDirectory) {
		this.rootDir = Paths.get(System.getProperty("user.dir"), fileDirectory);
		this.fileStorage = new JsonFileStorage<>(BinaryContent.class);
		fileStorage.init(rootDir);
	}

	private List<BinaryContent> findAll() {
		return fileStorage.load(rootDir.resolve(BINARYCONTENT_FILE));
	}

	@Override
	public BinaryContent save(BinaryContent binaryContent) {
		List<BinaryContent> contents = findAll();
		boolean updated = false;
		for (int i = 0; i < contents.size(); i++) {
			if (contents.get(i).getId().equals(binaryContent.getId())) {
				contents.set(i, binaryContent);
				updated = true;
				break;
			}
		}
		if (!updated) {
			contents.add(binaryContent);
		}
		fileStorage.save(rootDir.resolve(BINARYCONTENT_FILE), contents);
		return binaryContent;
	}

	@Override
	public Optional<BinaryContent> findById(UUID id) {
		List<BinaryContent> contents = findAll();
		for (BinaryContent bc : contents) {
			if (bc.getId().equals(id)) {
				return Optional.of(bc);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<BinaryContent> findByMessageId(UUID messageId) {
		List<BinaryContent> contents = findAll();
		List<BinaryContent> result = new ArrayList<>();
		for (BinaryContent bc : contents) {
			// 메시지가 있는 첨부파일인 경우에 messageId와 비교
			if (messageId.equals(bc.getMessageId())) {
				result.add(bc);
			}
		}
		return result;
	}

	/**
	 * 특정 작성자(authorId)가 등록한 BinaryContent 목록을 조회.
	 */
	@Override
	public List<BinaryContent> findByAuthorId(UUID authorId) {
		List<BinaryContent> contents = findAll();
		List<BinaryContent> result = new ArrayList<>();
		for (BinaryContent bc : contents) {
			if (bc.getAuthorId().equals(authorId)) {
				result.add(bc);
			}
		}
		return result;
	}

	@Override
	public Optional<BinaryContent> findByAuthorIdAndBinaryContentTypeAndMessageIdIsNull(UUID authorId,
		BinaryContentType binaryContentType
	) {
		List<BinaryContent> contents = findAll();
		for (BinaryContent content : contents) {
			if (content.getAuthorId().equals(authorId) && content.getBinaryContentType() == binaryContentType
				&& content.getMessageId() == null) {
				return Optional.of(content);
			}
		}
		return Optional.empty();
	}

	@Override
	public void deleteById(UUID id) {
		List<BinaryContent> contents = findAll();
		contents.removeIf(binaryContent -> binaryContent.getId().equals(id));
		fileStorage.save(rootDir.resolve(BINARYCONTENT_FILE), contents);
	}

	/**
	 * 특정 메시지와 관련된 모든 BinaryContent를 삭제.
	 */
	@Override
	public void deleteAllByMessageId(UUID messageId) {
		List<BinaryContent> contents = findAll();
		contents.removeIf(binaryContent -> binaryContent.getMessageId().equals(messageId));
		fileStorage.save(rootDir.resolve(BINARYCONTENT_FILE), contents);
	}
}
