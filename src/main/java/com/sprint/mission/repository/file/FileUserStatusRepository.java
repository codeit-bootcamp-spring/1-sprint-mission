package com.sprint.mission.repository.file;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.repository.UserStatusRepository;
import java.io.FileOutputStream;
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
public class FileUserStatusRepository implements UserStatusRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileUserStatusRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        UserStatus.class.getSimpleName());
    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new CustomException(ErrorCode.FILE_CREATE_ERROR);
      }
    }
  }

  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    Path path = resolvePath(userStatus.getId());
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
      oos.writeObject(userStatus);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
    return userStatus;
  }

  @Override
  public Optional<UserStatus> findById(UUID id) {
    Path path = resolvePath(id);
    if (Files.exists(path)) {
      try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
        return Optional.ofNullable((UserStatus) ois.readObject());
      } catch (IOException e) {
        throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
      } catch (ClassNotFoundException e) {
        throw new CustomException(ErrorCode.NO_SUCH_USER_STATUS_FILE);
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return findAll().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
              return (UserStatus) ois.readObject();
            } catch (IOException e) {
              throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
            } catch (ClassNotFoundException e) {
              throw new CustomException(ErrorCode.NO_SUCH_USER_STATUS_FILE);
            }
          })
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
  public void deleteByUserId(UUID userId) {
    this.findByUserId(userId)
        .ifPresent(userStatus -> this.deleteById(userStatus.getId()));
  }
}
