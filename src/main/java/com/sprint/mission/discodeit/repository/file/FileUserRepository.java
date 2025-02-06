package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.USER_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

  @Override
  public User create(User user) {
    List<User> users = FileUtil.loadAllFromFile(USER_FILE, User.class);
    users.add(user);
    FileUtil.saveAllToFile(USER_FILE, users);
    return user;
  }

  @Override
  public Optional<User> findById(String id) {
    List<User> users = FileUtil.loadAllFromFile(USER_FILE, User.class);
    return users.stream().filter(u -> u.getUUID().equals(id)).findAny();
  }

  @Override
  public List<User> findAll() {
    return FileUtil.loadAllFromFile(USER_FILE, User.class);
  }

  @Override
  public User update(User user) {
    List<User> users = FileUtil.loadAllFromFile(USER_FILE, User.class);
    User targetUser = users.stream()
        .filter(u -> u.getUUID().equals(user.getUUID()))
        .findAny()
        .orElseThrow(() -> new UserNotFoundException());
    users.remove(targetUser);
    users.add(user);
    FileUtil.saveAllToFile(USER_FILE, users);

    return user;
  }

  @Override
  public void delete(String userId) {
    List<User> users = FileUtil.loadAllFromFile(USER_FILE, User.class);
    User targetUser = users.stream()
        .filter(u -> u.getUUID().equals(userId))
        .findAny()
        .orElseThrow(() -> new UserNotFoundException());
    users.remove(targetUser);
    FileUtil.saveAllToFile(USER_FILE, users);
  }

  @Override
  public void clear() {
    File file = new File(USER_FILE);
    if (file.exists()) {
      file.delete();
    }
  }
}
