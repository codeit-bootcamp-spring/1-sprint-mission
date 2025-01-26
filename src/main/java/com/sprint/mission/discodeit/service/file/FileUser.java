package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class FileUser implements UserService {
  private Path fileDirectory;
  private FileBasic userFileBasic;

  public FileUser(Path directory) {
    this.fileDirectory = directory;
    this.userFileBasic = new FileBasic(fileDirectory);
  }


  @Override
  public void creat(User user) {
    List<User> userList = userFileBasic.load();
    Path filePath = fileDirectory.resolve(user.getId().toString().concat(".ser"));
    boolean duplication = userList.stream().anyMatch(user1 -> user1.getName().equals(user.getName()));
    if(!duplication) {
      try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
          ObjectOutputStream oos = new ObjectOutputStream(fos);
      ) {
        oos.writeObject(user);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    else {
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
  public UUID write(String name) {
    List<User> userList = userFileBasic.load();
    try {
      return userList.stream()
          .filter(user -> user.getName().equals(name)).findFirst().get().getId();
    }
    catch (Exception e) {
      System.out.println("There is no user with that name.");
      return null;
    }
  }

  @Override
  public List<User> allWrite() {
    List<User> userList = userFileBasic.load();
    return userList.stream()
        .map(User::new)
        .collect(Collectors.toList());
  }

  @Override
  public String getName(UUID userId) {
    return "";
  }
}
