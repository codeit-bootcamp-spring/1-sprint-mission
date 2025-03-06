package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileReadStatusRepository(
      @Value("${discodeit.repository.file-directory:data}") String fileDirectory
  ) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        ReadStatus.class.getSimpleName());
    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    Path path = resolvePath(readStatus.getId());
    try (
        FileOutputStream fos = new FileOutputStream(path.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      oos.writeObject(readStatus);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    ReadStatus readStatusNullable = null;
    Path path = resolvePath(id);
    if (Files.exists(path)) {
      try (
          FileInputStream fis = new FileInputStream(path.toFile());
          ObjectInputStream ois = new ObjectInputStream(fis)
      ) {
        readStatusNullable = (ReadStatus) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return Optional.ofNullable(readStatusNullable);
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
              return (ReadStatus) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
              throw new RuntimeException(e);
            }
          })
          .filter(readStatus -> readStatus.getUserId().equals(userId))
          .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
              return (ReadStatus) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
              throw new RuntimeException(e);
            }
          })
          .filter(readStatus -> readStatus.getChannelId().equals(channelId))
          .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean existsById(UUID id) {
    Path path = resolvePath(id);
    return Files.exists(path);
  }

  @Override
  public void deleteById(UUID id) {
    Path path = resolvePath(id);
    try {
      Files.delete(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
        .forEach(readStatus -> this.deleteById(readStatus.getId()));
  }
}
