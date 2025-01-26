package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import java.io.*;

public class FileUserRepository implements UserRepository {
  private static final String FILE_PATH = "user.ser";

  private final List<User> data;

  public FileUserRepository() {
    this.data = loadData();
  }

  public void save(User user){
    this.data.add(user);
  }

  public Optional<User> findById(UUID id) {
    return data.stream()
      .filter(user -> user.getId().equals(id))
      .findFirst();
  }

  public List<User> findAll() {
    return new ArrayList<>(data);
  }

  public void updateOne(User user, String name, int age, char gender) {
    if (user != null) {
      user.update(name, age, gender);
    } else {
      // 예외 처리 또는 로깅
      System.out.println("User is null!");
    }
  }

  public void deleteOne(UUID id) {
    data.removeIf(user -> user.getId().equals(id));
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