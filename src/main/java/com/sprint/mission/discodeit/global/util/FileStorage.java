package com.sprint.mission.discodeit.global.util;

import java.nio.file.Path;
import java.util.List;

public interface FileStorage<T> {
	void save(Path filePath, List<T> data);

	List<T> load(Path filePath);

	void init(Path filePath);
}
