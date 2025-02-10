package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileIO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier("file")
@AllArgsConstructor
public class FileUserRepository implements UserRepository {
  private final Path DIRECTORY = Paths.get("repository-data", "users");
  private final String EXTENSION = ".ser";
  private final FileIO fileIO;
  
  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }
  
  @Override
  public void save(User user) {
    File file = resolvePath(user.getId()).toFile();
    fileIO.saveToFile(file, user);
  }
  
  @Override
  public Optional<User> findById(UUID id) {
    return findAll().stream()
        .filter(u -> u.getId().equals(id))
        .findFirst();
  }
  
  @Override
  public Optional<User> findByName(String name) {
    return findAll().stream()
        .filter(u -> u.getName().equals(name))
        .findFirst();
  }
  
  @Override
  public List<User> findAll() {
    return fileIO.loadAllFromDirectory(DIRECTORY, EXTENSION, User.class);
  }
  
  @Override
  public void remove(UUID id) {
    File file = resolvePath(id).toFile();
    if (file.exists()) {
      fileIO.deleteFile(file);
    }
  }
}
