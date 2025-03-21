package com.sprint.mission.repository.file;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.repository.MessageRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "false")
@Repository
public class FileMessageRepository implements MessageRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileMessageRepository(
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
  public void save(Message message) {
    Path msDirectPath = getMsDirectPath(message.getId());
    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(
        getMsDirectPath(message.getId())))) {
      oos.writeObject(message);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
  }

  @Override
  public Optional<Message> findById(UUID messageId) {
    return Optional.ofNullable(readMessageFromFile(getMsDirectPath(messageId)));
  }

  @Override
  public List<Message> findAll() {
    try {
      return Files.exists(DIRECTORY)
          ? Files.list(DIRECTORY)
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(this::readMessageFromFile)
          .collect(Collectors.toCollection(ArrayList::new))

          : new ArrayList<>();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
  }

  @SneakyThrows
  public List<Message> findMessagesInChannel(UUID channelId) {
    return findAll().stream()
        .filter(message ->
            message.getChannelId() == channelId)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public void delete(UUID messageId) {
    try {
      Files.delete(getMsDirectPath(messageId));
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
    }
  }

  @Override
  public boolean existsById(UUID messageId) {
    return Files.exists(getMsDirectPath(messageId));
  }

  /**
   * 편의 메서드
   */
  private Path getMsDirectPath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }

  private Message readMessageFromFile(Path msPath) {
    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(msPath))) {
      return (Message) ois.readObject();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    } catch (ClassNotFoundException e) {
      throw new CustomException(ErrorCode.NO_SUCH_MESSAGE_FILE);
    }
  }

  /**
   * 메시지 디렉토리 생성
   */
  public void createDirectory() {
    if (Files.exists(DIRECTORY)) {
      try {
        Files.list(DIRECTORY).forEach(
            file -> {
              try {
                Files.delete(file);
              } catch (IOException e) {
                throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
              }
            });
      } catch (IOException e) {
        throw new CustomException(ErrorCode.FAIL_TO_INIT_DIRECTORY);
      }
    } else {
      try {
        Files.createDirectory(DIRECTORY);
      } catch (IOException e) {
        throw new CustomException(ErrorCode.DIRECTORY_CREATE_ERROR);
      }
    }
  }

}
