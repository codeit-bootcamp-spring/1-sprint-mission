package com.sprint.mission.discodeit.service;

import java.util.List;

public interface FileService<T> {
    List<T> readFromFile();
    void writeToFile(List<T> data);
}
