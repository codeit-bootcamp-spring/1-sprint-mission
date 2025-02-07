package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.READ_STATUS_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository{


  @Override
  public ReadStatus save(ReadStatus status) {
    List<ReadStatus> statuses = FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class);
    statuses.removeIf(s -> s.getUUID().equals(status.getUUID()));
    statuses.add(status);
    FileUtil.saveAllToFile(READ_STATUS_FILE, statuses);
    return status;
  }

  @Override
  public Optional<ReadStatus> findById(String id) {
    List<ReadStatus> statuses = FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class);

    return statuses.stream()
        .filter(status -> status.getUUID().equals(id))
        .findAny();
  }

  @Override
  public List<ReadStatus> findByUserId(String userId) {
    List<ReadStatus> statuses = FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class);

    return statuses.stream()
        .filter(status -> status.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<ReadStatus> findByChannelId(String id) {
    List<ReadStatus> statuses = FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class);
    return statuses.stream()
        .filter(status -> status.getChannelId().equals(id))
        .toList();
  }

  @Override
  public Optional<ReadStatus> findByChannelIdAndUserId(String channelId, String userId) {
    List<ReadStatus> statuses = FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class);

    return statuses.stream()
        .filter(status -> status.getChannelId().equals(channelId)
            && status.getUserId().equals(userId))
        .findAny();
  }

  @Override
  public List<ReadStatus> findAll() {
    return FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class);
  }

  @Override
  public void deleteByUserId(String id) {
    List<ReadStatus> statuses = FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class);
    statuses.removeIf(status -> status.getUserId().equals(id));
    FileUtil.saveAllToFile(READ_STATUS_FILE, statuses);
  }

  @Override
  public void deleteByChannelId(String channelId) {
    List<ReadStatus> statuses = FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class);
    statuses.removeIf(status -> status.getChannelId().equals(channelId));
    FileUtil.saveAllToFile(READ_STATUS_FILE, statuses);
  }

  @Override
  public void clear(){

  }
}
