package com.sprint.mission.repository.file;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.repository.ChannelRepository;
import java.nio.file.Paths;
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
public class FileChannelRepository implements ChannelRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileChannelRepository(
      @Value("${discodeit.repository.file-directory:data}") String fileDirectory) {

    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        ReadStatus.class.getSimpleName());
    if (!Files.exists(DIRECTORY)) {
      try {
        Files.createDirectory(DIRECTORY);
      } catch (IOException e) {
        throw new CustomException(ErrorCode.DIRECTORY_CREATE_ERROR);
      }
    }
  }

  @Override
  public Channel save(Channel channel) {
    Path channelPath = getChannelDirectPath(channel.getId());
    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(channelPath))) {
      oos.writeObject(channel);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
    return channel;
  }


  @Override
  public Optional<Channel> findById(UUID id) {
    try (ObjectInputStream ois = new ObjectInputStream(
        Files.newInputStream(getChannelDirectPath(id)))) {
      return Optional.ofNullable((Channel) ois.readObject());
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    } catch (ClassNotFoundException e) {
      throw new CustomException(ErrorCode.NO_SUCH_CHANNEL_FILE);
    }
  }

  @Override
  public List<Channel> findAll() {
    try {
      return Files.exists(DIRECTORY)
          ? Files.list(DIRECTORY)
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(this::readChannelFromFile)
          .collect(Collectors.toCollection(ArrayList::new))
          : new ArrayList<>();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    }
  }


  @Override
  public void delete(UUID channelId) {
    try {
      Files.delete(getChannelDirectPath(channelId));
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
    }
  }

  @Override
  public boolean existsById(UUID channelId) {
    return Files.exists(getChannelDirectPath(channelId));
  }

  /**
   * 편의 메서드
   */
  private Path getChannelDirectPath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }


  public Channel readChannelFromFile(Path channelPath) {
    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(channelPath))) {
      return (Channel) ois.readObject();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
    } catch (ClassNotFoundException e) {
      throw new CustomException(ErrorCode.NO_SUCH_CHANNEL_FILE);
    }
  }

  /**
   * 디렉토리 생성 - 기존 디렉토리의 파일들 삭제 (테스트용)
   */
  public void createChannelDirectory() {
    if (Files.exists(DIRECTORY)) {
      try {
        Files.list(DIRECTORY)
            .forEach(file -> {
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

