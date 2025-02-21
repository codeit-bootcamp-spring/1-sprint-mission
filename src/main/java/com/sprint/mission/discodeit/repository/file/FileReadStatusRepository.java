package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.FileException;
import com.sprint.mission.discodeit.repository.AbstractFileRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileReadStatusRepository extends AbstractFileRepository<ReadStatus> implements ReadStatusRepository {


  public FileReadStatusRepository(@Value("${app.file.read-status-file}") String filePath) {
    super(filePath);
  }

  @Override
  public ReadStatus save(ReadStatus status) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);
    statuses.removeIf(s -> s.getId().equals(status.getId()));
    statuses.add(status);
    saveAll(statuses);
    return status;
  }

  @Override
  public Optional<ReadStatus> findById(String id) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);

    return statuses.stream()
        .filter(status -> status.getId().equals(id))
        .findAny();
  }

  @Override
  public List<ReadStatus> findByUserId(String userId) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);

    return statuses.stream()
        .filter(status -> status.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<ReadStatus> findByChannelId(String id) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);
    return statuses.stream()
        .filter(status -> status.getChannelId().equals(id))
        .toList();
  }

  @Override
  public Optional<ReadStatus> findByChannelIdAndUserId(String channelId, String userId) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);

    return statuses.stream()
        .filter(status -> status.getChannelId().equals(channelId)
            && status.getUserId().equals(userId))
        .findAny();
  }

  @Override
  public List<ReadStatus> findAll() {
    return loadAll(ReadStatus.class);
  }

  @Override
  public void deleteByUserId(String id) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);
    statuses.removeIf(status -> status.getUserId().equals(id));
    saveAll(statuses);
  }

  @Override
  public void deleteByChannelId(String channelId) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);
    statuses.removeIf(status -> status.getChannelId().equals(channelId));
    saveAll(statuses);
  }

  @Override
  public void clear() {
    Path path = Paths.get(getFilePath());

    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new FileException();
    }
  }
}
