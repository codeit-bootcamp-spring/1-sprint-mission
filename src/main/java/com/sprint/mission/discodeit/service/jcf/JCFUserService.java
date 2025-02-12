package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserService implements UserService {
  private final UserRepository userRepository;
  public JCFUserService(UserRepository userRepository){this.userRepository = userRepository;}

  @Override
  public boolean createUser(User user) {
    boolean created = userRepository.save(user);
    if(created){
      System.out.println("생성된 회원: " + user);
      return true;
    } else{
      return false;
    }
  }

  @Override
  public Optional<User> readUser(UUID id) {
    Optional<User> usr = userRepository.findById(id);
    usr.ifPresent(u -> System.out.println("조회된 회원: " + u));
    return usr;
  }

  @Override
  public List<User> readAllUsers() {
    List<User> users = userRepository.findAll();
    if(users != null && !users.isEmpty()){
      System.out.println("전체 회원 목록: " + users);
      return users;
    } else {
      System.out.println("회원 목록이 비어 있습니다.");
      return Collections.emptyList(); // 비어 있을 경우 빈 리스트 반환
    }
  }

  @Override
  public void updateUser(UUID id, String name, int age, Gender gender) {
    boolean updated = userRepository.updateOne(id, name, age, gender);
    if (updated) {
      Optional<User> usr = userRepository.findById(id);
      usr.ifPresent(u -> System.out.println("수정된 회원: " + u));
      List<User> allUsers = userRepository.findAll();
      System.out.println("수정 후 전체 회원 목록: " + allUsers);
    }
  }

  @Override
  public void deleteUser(UUID id) {
    boolean deleted = userRepository.deleteOne(id);
    if (deleted){
      Optional<User> usr = userRepository.findById(id);
      System.out.println("삭제된 회원: " + usr); // 존재안하면 안하는데로 Optional.isEmpty 가 뜨는지 확인
      List<User> allUsers = userRepository.findAll();
      System.out.println("삭제 후 전체 회원 목록: " + allUsers);
    }
  }
}