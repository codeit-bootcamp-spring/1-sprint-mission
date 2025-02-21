package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.AbstractFileRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.READ_STATUS_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileReadStatusRepository extends AbstractFileRepository<ReadStatus> implements ReadStatusRepository{


  public FileReadStatusRepository(@Value("${app.file.read-status-file}") String filePath) {
    super(filePath);
  }

  @Override
  public ReadStatus save(ReadStatus status) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);
    statuses.removeIf(s -> s.getUUID().equals(status.getUUID()));
    statuses.add(status);
    saveAll(statuses);
    return status;
  }

  @Override
  public Optional<ReadStatus> findById(String id) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);;

    return statuses.stream()
        .filter(status -> status.getUUID().equals(id))
        .findAny();
  }

  @Override
  public List<ReadStatus> findByUserId(String userId) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);;

    return statuses.stream()
        .filter(status -> status.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<ReadStatus> findByChannelId(String id) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);;
    return statuses.stream()
        .filter(status -> status.getChannelId().equals(id))
        .toList();
  }

  @Override
  public Optional<ReadStatus> findByChannelIdAndUserId(String channelId, String userId) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);;

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
    List<ReadStatus> statuses = loadAll(ReadStatus.class);;
    statuses.removeIf(status -> status.getUserId().equals(id));
    saveAll(statuses);
  }

  @Override
  public void deleteByChannelId(String channelId) {
    List<ReadStatus> statuses = loadAll(ReadStatus.class);;
    statuses.removeIf(status -> status.getChannelId().equals(channelId));
    saveAll(statuses);
  }

  @Override
  public void clear(){
    File file = new File(getFilePath());
    if(file.exists()) file.delete();
  }
}
