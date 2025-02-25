package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.AbstractFileRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.ErrorConstant.USER_NOT_FOUND;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file",  matchIfMissing = true)
public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository{

  public FileUserRepository(@Value("${app.file.user-file}") String filePath) {
    super(filePath);
  }

  @Override
  public User create(User user) {
    List<User> users = loadAll(User.class);

    users.add(user);
    saveAll(users);

    return user;
  }

  @Override
  public Optional<User> findById(String id) {
    List<User> users = loadAll(User.class);
    return users.stream().filter(u -> u.getId().equals(id)).findAny();
  }

  @Override
  public Optional<User> findByUsername(String username) {
    List<User> users = loadAll(User.class);

    return users.stream().filter(u -> u.getUsername().equals(username)).findAny();
  }

  @Override
  public List<User> findAll() {
    return loadAll(User.class);
  }

  @Override
  public User update(User user) {
    List<User> users = loadAll(User.class);

    User targetUser = users.stream()
        .filter(u -> u.getId().equals(user.getId()))
        .findAny()
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    users.remove(targetUser);
    users.add(user);
    saveAll(users);

    return user;
  }

  @Override
  public void delete(String userId) {
    List<User> users = loadAll(User.class);
    User targetUser = users.stream()
        .filter(u -> u.getId().equals(userId))
        .findAny()
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    users.remove(targetUser);
    saveAll(users);
  }

  @Override
  public void clear() {
    File file = new File(getFilePath());
    if(file.exists()) file.delete();
  }

  @PostConstruct
  private void postContruct(){
    clear();
  }
}
