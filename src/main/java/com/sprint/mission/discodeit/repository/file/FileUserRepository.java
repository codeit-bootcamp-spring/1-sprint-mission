package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCF_Message;
import com.sprint.mission.discodeit.service.file.FileBasic;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {

  private Path fileDirectory;
  private FileBasic userFileBasic;

  public FileUserRepository(Path directory) {
    this.fileDirectory = directory;
    this.userFileBasic = new FileBasic(fileDirectory);
  }


  @Override
  public void creat(User user) {
    List<User> userList = userFileBasic.load();
    Set<String> nameSet = userList.stream()
        .map(User::getName)
        .collect(Collectors.toSet());
    Path filePath = fileDirectory.resolve(user.getId().toString().concat(".ser"));
    if (!nameSet.add(user.getName())) {
      try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
          ObjectOutputStream oos = new ObjectOutputStream(fos);
      ) {
        oos.writeObject(user);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("name duplication!");
    }

  }


  @Override
  public void delete(UUID userId) {
    try {
      Optional<Path> userDelete = Files.list(fileDirectory)
          .filter(path -> path.getFileName().toString().equals(userId + ".ser"))
          .findFirst();
      if(userDelete.isPresent()) {
        Files.delete(userDelete.get());
      }
      else {
        System.out.println("File not found for Id");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(UUID userId, String name) {
    try {
      Optional<Path> updateUser = Files.list(fileDirectory)
          .filter(path -> path.getFileName().toString().equals(userId + ".ser"))
          .findFirst();
      if(updateUser.isPresent()) {
        User user = (User) userFileBasic.deserialization(updateUser.get());
        user.updateName(name);
        creat(user);
      }
      else {
        System.out.println("File not found for Id");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public UUID findByName(String name) {
    List<User> userList = userFileBasic.load();
    Optional<User> user = userList.stream().filter(user1 -> user1.getName().equals(name)).findFirst();
    if (user.isPresent()) {
      return user.get().getId();
    }
    else {
      System.out.println("Name not found");
      return null;
    }
  }

  @Override
  public List<User> findByAll() {
    List<User> userList = userFileBasic.load();
    return userList.stream()
        .map(User::new)
        .collect(Collectors.toList());
  }
}
