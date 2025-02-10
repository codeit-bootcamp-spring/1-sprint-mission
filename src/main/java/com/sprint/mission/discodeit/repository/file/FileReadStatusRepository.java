package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.FileIO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier("file")
@AllArgsConstructor
public class FileReadStatusRepository implements ReadStatusRepository {
  private final Path DIRECTORY = Paths.get("repository-data", "read-statuses");
  private final String EXTENSION = ".ser";
  private final FileIO fileIO;
  
  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }
  
  private List<ReadStatus> findAll() {
    return fileIO.loadAllFromDirectory(DIRECTORY, EXTENSION, ReadStatus.class);
  }
  
  @Override
  public void save(ReadStatus readStatus) {
    File file = resolvePath(readStatus.getId()).toFile();
    fileIO.saveToFile(file, readStatus);
  }
  
  @Override
  public Optional<ReadStatus> findById(UUID readStatusId) {
    return findAll().stream()
        .filter(r -> r.getId().equals(readStatusId))
        .findFirst();
  }
  
  @Override
  public List<ReadStatus> findAllByUser(UUID userId) {
    return findAll().stream()
        .filter(r -> r.getUserid().equals(userId))
        .toList();
  }
  
  @Override
  public void remove(UUID channelId) {
    UUID readStatusId = findAll().stream()
        .filter(r -> r.getChannelId().equals(channelId))
        .findFirst()
        .map(ReadStatus::getId)
        .orElse(null);
    
    File file = resolvePath(readStatusId).toFile();
    if (file.exists()) {
      fileIO.deleteFile(file);
    }
  }
}
