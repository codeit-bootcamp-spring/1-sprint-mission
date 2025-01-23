package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.FileException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.USER_FILE;
import static com.sprint.mission.discodeit.constant.UserConstant.NO_MATCHING_USER;
import static com.sprint.mission.discodeit.constant.UserConstant.PASSWORD_MATCH_ERROR;

public class FileUserService implements UserService {

  private static volatile FileUserService userRepository;

  private FileUserService() {
  }

  public static FileUserService getInstance() {
    if (userRepository == null) {
      synchronized (FileUserService.class) {
        if (userRepository == null) {
          userRepository = new FileUserService();
          initFile();
        }
      }
    }
    return userRepository;
  }

  private static void initFile() {
    File file = new File(USER_FILE);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException("파일 생성중 오류." + e.getMessage());
      }
    }
  }

  @Override
  public User createUser(User user) {

    if (!validateUser(user)) {
      throw new UserValidationException();
    }

    try {
      user.setPassword(PasswordEncryptor.hashPassword(user.getPassword()));
      saveUserToFile(user);
    } catch (FileException e) {
      e.printStackTrace();
    }

    return user;
  }

  @Override
  public Optional<User> readUserById(String id) {
    return loadAllUser().stream().filter(u -> u.getUUID().equals(id)).findAny();
  }

  @Override
  public List<User> readAllUsers() {
    return loadAllUser();
  }

  @Override
  public void updateUser(String id, UserUpdateDto updatedUser, String originalPassword) {
    List<User> users = loadAllUser();
    User targetUser = users.stream()
        .filter(u -> u.getUUID().equals(id)).findAny().orElseThrow(() ->
            new UserValidationException(NO_MATCHING_USER)
        );

    if (!PasswordEncryptor.checkPassword(originalPassword, targetUser.getPassword())) {
      throw new UserValidationException(PASSWORD_MATCH_ERROR);
    }

    synchronized (targetUser) {
      updatedUser.getUsername().ifPresent(targetUser::setUsername);
      updatedUser.getPassword().map(PasswordEncryptor::hashPassword).ifPresent(targetUser::setPassword);
      updatedUser.getEmail().ifPresent(targetUser::setEmail);
      updatedUser.getNickname().ifPresent(targetUser::setNickname);
      updatedUser.getPhoneNumber().ifPresent(targetUser::setPhoneNumber);
      updatedUser.getProfilePictureURL().ifPresent(targetUser::setProfilePictureURL);
      updatedUser.getDescription().ifPresent(targetUser::setDescription);

      targetUser.setUpdatedAt(System.currentTimeMillis());
    }
    saveUserToFile(users);
  }

  @Override
  public void deleteUser(String id, String password) {
    List<User> users = loadAllUser();
    User user = users.stream().filter(u -> u.getUUID().equals(id)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));
    users.remove(user);
    saveUserToFile(users);

    if (PasswordEncryptor.checkPassword(password, user.getPassword())) {
      users.remove(user);
      saveUserToFile(users);
    } else {
      throw new UserValidationException(PASSWORD_MATCH_ERROR);
    }
  }

  private boolean validateUser(User user) {
    List<User> users = loadAllUser();
    return users.stream().noneMatch(existingUser ->
        existingUser.getUsername().equals(user.getUsername())
            || existingUser.getEmail().equals(user.getEmail())
            || existingUser.getPhoneNumber().equals(user.getPhoneNumber())
    );
  }

  private void saveUserToFile(User user) throws FileException {
    List<User> users = loadAllUser();
    users.add(user);
    saveUserToFile(users);
  }


  private List<User> loadAllUser() {

    List<User> users = new ArrayList<>();

    try (FileInputStream fos = new FileInputStream(USER_FILE);
         ObjectInputStream ois = new ObjectInputStream(fos)) {
      while (true) {
        try {
          User user = (User) ois.readObject();
          users.add(user);
        } catch (EOFException e) {
          break;
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("직렬화된 객체 클래스가 존재하지 않습니다.", e);
        }
      }
    } catch (IOException e) {
      return users;
    }
    return users;
  }

  private void saveUserToFile(List<User> users) {
    try (
        FileOutputStream fos = new FileOutputStream(USER_FILE);
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      for (User u : users) {
        oos.writeObject(u);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
