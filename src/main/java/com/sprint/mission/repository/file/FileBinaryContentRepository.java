package com.sprint.mission.repository.file;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.BinaryContent;
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
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileBinaryContentRepository(
      @Value("${discodeit.repository.file-directory:data}") String fileDirectory
  ) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        BinaryContent.class.getSimpleName());
    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new CustomException(ErrorCode.FILE_CREATE_ERROR);
      }
    }
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(
        resolvePath(binaryContent.getId())))) {
      oos.writeObject(binaryContent);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
    return binaryContent;
  }

  @Override
  public Optional<BinaryContent> findById(UUID id) {
    BinaryContent binaryContentNullable = null;
    Path path = resolvePath(id);
    if (Files.exists(path)) {
      try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
        binaryContentNullable = (BinaryContent) ois.readObject();
      } catch (IOException e) {
        throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
      } catch (ClassNotFoundException e) {
        throw new CustomException(ErrorCode.NO_SUCH_USER_FILE);
      }
    }
    return Optional.ofNullable(binaryContentNullable);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
              return (BinaryContent) ois.readObject();
            } catch (IOException e) {
              throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
            } catch (ClassNotFoundException e) {
              throw new CustomException(ErrorCode.NO_SUCH_USER_FILE);
            }
          })
          .filter(content -> ids.contains(content.getId()))
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
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
  }

  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }
}
