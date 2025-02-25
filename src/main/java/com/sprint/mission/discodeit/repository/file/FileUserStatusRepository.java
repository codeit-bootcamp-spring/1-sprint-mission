package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.FileIO;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
  private final Path DIRECTORY = Paths.get("repository-data", "users");
  private final String EXTENSION = ".ser";
  private final FileIO fileIO;
  
  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }
  
  @Override
  public void save(UserStatus userStatus) {
    File file = resolvePath(userStatus.getId()).toFile();
  }
  
  @Override
  public Optional<UserStatus> findById(UUID userStatusId) {
    return findAll().stream()
        .filter(u -> u.getId().equals(userStatusId))
        .findFirst();
  }
  
  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return findAll().stream()
        .filter(u -> u.getUserId().equals(userId))
        .findFirst();
  }
  
  @Override
  public List<UserStatus> findAll() {
    List<UserStatus> userStatuses = fileIO.loadAllFromDirectory(DIRECTORY, EXTENSION, UserStatus.class);
    return new ArrayList<>(userStatuses);
  }
  
  @Override
  public boolean isOnline(UUID userId) {
    if (findByUserId(userId).isPresent()) {
      return findByUserId(userId).get().isOnline();
    }
    throw new NoSuchElementException("user status not found with user id: " + userId);
  }
  
  @Override
  public void remove(UUID userId) {
    File file = resolvePath(userId).toFile();
    if (file.exists()) {
      file.delete();
    }
  }
}
