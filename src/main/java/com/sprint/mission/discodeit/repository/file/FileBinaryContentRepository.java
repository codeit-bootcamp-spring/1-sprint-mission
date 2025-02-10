package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileIO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier("file")
@AllArgsConstructor
public class FileBinaryContentRepository implements BinaryContentRepository {
  private final Path DIRECTORY = Paths.get("repository-data", "binary-contents");
  private final String EXTENSION = ".ser";
  private final FileIO fileIO;
  
  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }
  
  private List<BinaryContent> loadAll() {
    return fileIO.loadAllFromDirectory(DIRECTORY, EXTENSION, BinaryContent.class);
  }
  
  @Override
  public void save(BinaryContent binaryContent) {
    File file = resolvePath(binaryContent.getId()).toFile();
    fileIO.saveToFile(file, binaryContent);
  }
  
  @Override
  public Optional<BinaryContent> findById(UUID binaryContentId) {
    return loadAll().stream()
        .filter(b -> b.getId().equals(binaryContentId))
        .findFirst();
  }
  
  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = new ArrayList<>();
    binaryContentIds.forEach(i -> {
      loadAll().stream()
          .filter(b -> b.getId().equals(i))
          .findFirst()
          .ifPresent(binaryContents::add);
    });
    return binaryContents;
  }
  
  @Override
  public void remove(UUID binaryContentId) {
    File file = resolvePath(binaryContentId).toFile();
    if (file.exists()) {
      fileIO.deleteFile(file);
    }
  }
}
