package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import java.io.*;

public class FileUserService implements UserService {
  private static final String FILE_PATH = "user.ser";
  private final UserRepository userRepository;
  private final List<User> data;

  public FileUserService(UserRepository userRepository){
    this.userRepository = userRepository;
    this.data = loadData();
  }

  @Override
  public void createUser(User user) {
    userRepository.save(user);
    data.add(user);
    saveData();
  }

  @Override
  public Optional<User> readUser(UUID id) {
    return userRepository.findById(id);
  }

  @Override
  public List<User> readAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public void updateUser(User user, String name, int age, Gender gender) {
    userRepository.updateOne(user, name, age, gender);
    saveData();
  }

  @Override
  public void deleteUser(UUID id) {
    userRepository.deleteOne(id);
    saveData();
  }

  // 파일에 객체 직렬화하기
  public void saveData(){
    try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
         ObjectOutputStream oos = new ObjectOutputStream(fos);
    ) {
      oos.writeObject(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 파일에 객체 역직렬화하기
  public List<User> loadData(){
    try (FileInputStream fis = new FileInputStream(FILE_PATH);
         ObjectInputStream ois = new ObjectInputStream(fis)) {
      return (List<User>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }
}