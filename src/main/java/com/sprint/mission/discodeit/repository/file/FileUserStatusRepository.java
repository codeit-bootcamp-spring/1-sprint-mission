package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.USER_STATUS_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository{


  @Override
  public UserStatus save(UserStatus userStatus) {
    List<UserStatus> statuses = FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class);

    statuses.removeIf(status -> status.getUUID().equals(userStatus.getUUID()));
    statuses.add(userStatus);

    FileUtil.saveAllToFile(USER_STATUS_FILE, statuses);
    return userStatus;
  }

  @Override
  public Optional<UserStatus> findById(String id) {
    List<UserStatus> statuses = FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class);

    return statuses.stream()
        .filter(status -> status.getUUID().equals(id))
        .findFirst();
  }

  @Override
  public Optional<UserStatus> findByUserId(String id) {
    List<UserStatus> statuses = FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class);
    return statuses.stream()
        .filter(status -> status.getUserId().equals(id))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class);
  }

  @Override
  public void deleteByUserId(String id) {
    List<UserStatus> statuses = FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class);
    statuses.removeIf(status->status.getUserId().equals(id));
    FileUtil.saveAllToFile(USER_STATUS_FILE, statuses);
  }

  @Override
  public void deleteById(String id) {
    List<UserStatus> statuses = FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class);
    statuses.removeIf(status->status.getUUID().equals(id));
    FileUtil.saveAllToFile(USER_STATUS_FILE, statuses);
  }

  @Override
  public void clear() {

  }
}
