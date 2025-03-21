package com.sprint.mission.repository.file;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.repository.ReadStatusRepository;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

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
        throw new CustomException(ErrorCode.FAIL_TO_INIT_DIRECTORY);
      }
    }
  }

  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    Path path = resolvePath(readStatus.getId());
    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
      oos.writeObject(readStatus);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_WRITE_ERROR);
    }
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    Path path = resolvePath(id);
    if (Files.exists(path)) {
      try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
        return Optional.ofNullable((ReadStatus) ois.readObject());
      } catch (IOException e) {
        throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
      } catch (ClassNotFoundException e) {
        throw new CustomException(ErrorCode.NO_SUCH_READ_STATUS_FILE);
      }
    }
    throw new CustomException(ErrorCode.NO_SUCH_READ_STATUS_FILE);
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
              return (ReadStatus) ois.readObject();
            } catch (IOException e) {
              throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
            } catch (ClassNotFoundException e) {
              throw new CustomException(ErrorCode.NO_SUCH_USER_STATUS_FILE);
            }
          })
          .filter(readStatus -> readStatus.getUserId().equals(userId))
          .toList();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
              return (ReadStatus) ois.readObject();
            } catch (IOException e) {
              throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
            } catch (ClassNotFoundException e) {
              throw new CustomException(ErrorCode.NO_SUCH_READ_STATUS_FILE);
            }
          })
          .filter(readStatus -> readStatus.getChannelId().equals(channelId))
          .toList();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
  }

  @Override
  public boolean existsById(UUID id) {
    return Files.exists(resolvePath(id));
  }

  @Override
  public void deleteById(UUID id) {
    try {
      Files.delete(resolvePath(id));
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
    }
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
        .forEach(readStatus -> this.deleteById(readStatus.getId()));
  }
}
