package com.sprint.mission.discodeit.service.file;

import java.util.List;

public interface FileService<T> {
    List<T> loadFromFile();
    boolean saveToFile(List<T> data);
}
