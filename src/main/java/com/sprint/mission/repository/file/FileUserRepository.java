package com.sprint.mission.repository.file;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepository implements UserRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileUserRepository(
      @Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        ReadStatus.class.getSimpleName());
    if (!Files.exists(DIRECTORY)){
      try {
        Files.createDirectory(DIRECTORY);
      } catch (IOException e) {
        throw new CustomException(ErrorCode.DIRECTORY_CREATE_ERROR);
      }
    }
  }

  @Override
  public User save(User user) {

    Path path = DIRECTORY.resolve(user.getId() + ".ser");
    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
      oos.writeObject(user);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_WRITE_ERROR);
    }
    return user;
  }

  /**
   * 조회
   */
  @Override
  public Optional<User> findById(UUID userId) {
    Path userFilePath = getUserFilePath(userId);
    if (!Files.exists(userFilePath)) {
      throw new CustomException(ErrorCode.NO_SUCH_USER_FILE);
    }

    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(userFilePath))) {
      return Optional.ofNullable(ois.readObject())
          .map(obj -> (User) obj);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    } catch (ClassNotFoundException e) {
      throw new CustomException(ErrorCode.NO_SUCH_MESSAGE_FILE);
    }
  }

  @Override
  public List<User> findAll() {
    try {
      return Files.exists(DIRECTORY)
          ? Files.list(DIRECTORY)
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(this::readUserFromFile)
          .collect(Collectors.toCollection(ArrayList::new))
          : new ArrayList<>();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
  }

  @Override
  public void delete(UUID userId) {
    try {
      Files.delete(getUserFilePath(userId));
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
    }
  }

  @SneakyThrows
  @Override
  public boolean existsById(UUID userId) {
    return Files.exists(getUserFilePath(userId));
  }

  /**
   * 편의 메서드 모음
   */
  private User readUserFromFile(Path filePath) {
    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
      return (User) ois.readObject();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    } catch (ClassNotFoundException e) {
      throw new CustomException(ErrorCode.NO_SUCH_MESSAGE_FILE);
    }
  }

  private Path getUserFilePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }
}

