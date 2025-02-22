package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
  private final Map<UUID, User> data;
  public FileUserRepository(SerializationUtil<UUID, User> util) {
    this.data = util.loadData(); // 이 부분, filePath 매개변수 이해 잘 안됨
  }


  @Override
  public User save(User user){
    data.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<User> findById(UUID userId){
    return Optional.ofNullable(this.data.get(userId));
//   **map이니까 key 기반 빠른 조회 가능** 만약 data가 리스트였으면 이렇게 반복문 돌려야 함!
//    for (User user : data){
//      if(user.user().getId().equals(id)){
//        return Optional.of(user);
//      }
//    }
//    return Optional.empty();
  }

  @Override
  public Optional<User> findByUsername(String userName) {
    return this.findAll().stream()
            .filter(user -> user.getUsername().equals(userName))
            .findFirst();
  }

  @Override
  public boolean existsById(UUID userId) {
    return this.data.containsKey(userId);
  }

  @Override
  public List<User> findAll(){
    List<User> users = new ArrayList<>(data.values());
    return users;
    // return this.data.values().stream().toList();
  }

  @Override
  public void deleteById(UUID userId) {
    this.data.remove(userId);
  }

  @Override
  public boolean existsByUsername(String userName){
    return this.findAll().stream().anyMatch(user -> user.getEmail().equals(userName));
  }

  @Override
  public boolean existsByEmail(String email) {
    return this.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
  }
}