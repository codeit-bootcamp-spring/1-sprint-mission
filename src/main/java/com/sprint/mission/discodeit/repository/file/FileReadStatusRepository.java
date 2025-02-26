package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.config.FileRepositoryCondition;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
@FileRepositoryCondition
public class FileReadStatusRepository implements ReadStatusRepository {

  private final FileStorage fileStorage;
  private final Path directory;
  private static final String SUBDIRECTORY = "readStatus";

  public FileReadStatusRepository(FileStorage fileStorage) {
    this.fileStorage = fileStorage;
    directory = fileStorage.getDirectory(SUBDIRECTORY);
    fileStorage.init(directory);
  }

  @Override
  public ReadStatus saveReadStatus(ReadStatus readStatus) {
    Path filePath = fileStorage.getFilePath(directory, readStatus.getId());
    return fileStorage.save(filePath, readStatus);
  }

  @Override
  public ReadStatus findReadStatusById(UUID readStatusId) {
    List<ReadStatus> readStatusList = fileStorage.load(directory);

    return readStatusList.stream()
        .filter(readStatus -> readStatus.getId().equals(readStatusId))
        .findFirst().orElse(null);
  }

  @Override
  public Optional<ReadStatus> findReadStatusByUserId(UUID userId) {
    List<ReadStatus> readStatusList = fileStorage.load(directory);

    return readStatusList.stream()
        .filter(readStatus -> readStatus.getUser().getId().equals(userId))
        .findFirst();
  }

  @Override
  public List<ReadStatus> findAllReadStatusByUserId(UUID userId) {
    return null;
  }

  @Override
  public void removeReadStatus(UUID readStatusId) {
    ReadStatus readStatus = findReadStatusById(readStatusId);
    fileStorage.remove(directory, readStatus);
  }
}
