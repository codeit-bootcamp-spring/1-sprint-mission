package com.sprint.mission.discodeit.binaryContent.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.global.util.FileStorage;
import com.sprint.mission.discodeit.global.util.JsonFileStorage;
import com.sprint.mission.discodeit.binaryContent.entity.BinaryContent;

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
	public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
		List<BinaryContent> allContents = findAll(); // 모든 BinaryContent 로드
		List<BinaryContent> result = new ArrayList<>();

		// 주어진 ID 리스트에 포함된 BinaryContent만 필터링
		for (BinaryContent content : allContents) {
			if (ids.contains(content.getId())) {
				result.add(content);
			}
		}

		return result;
	}

	@Override
	public boolean existById(UUID id) {
		return findById(id).isPresent();
	}

	@Override
	public void deleteById(UUID id) {
		List<BinaryContent> contents = findAll();
		contents.removeIf(binaryContent -> binaryContent.getId().equals(id));
		fileStorage.save(rootDir.resolve(BINARYCONTENT_FILE), contents);
	}
}
