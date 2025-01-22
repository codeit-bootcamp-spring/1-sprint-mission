package com.sprint.mission.discodeit.service.file;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FileService<T> {
    Map<UUID, T> loadFromFile();
    boolean saveToFile(Map<UUID, T> data);
}
