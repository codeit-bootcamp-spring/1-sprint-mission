package com.sprint.mission.discodeit.service;

import java.nio.file.Path;
import java.util.List;

public interface FileStorage<T> {
    void save(Path filePath, List<T> data);
    List<T> load(Path directory);
    void init(Path directory);
}
