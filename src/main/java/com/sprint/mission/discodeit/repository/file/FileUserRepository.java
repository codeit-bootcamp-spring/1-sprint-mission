package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserRepository implements UserRepository {
  private final List<User> data;
  public FileUserRepository(SerializationUtil<User> util) {
    this.data = util.loadData(); // 이 부분, filePath 매개변수 이해 잘 안됨
  }

  @Override
  public boolean save(User user){
    try {
      if (user == null) {
        throw new IllegalArgumentException("회원이 null일 수 없습니다.");
      }
      return data.add(user); // 성공하면 true 반환
    } catch(IllegalArgumentException e) {
      System.out.println("회원 생성 실패: " + e.getMessage());
    } catch(Exception e) {
      System.out.println("알 수 없는 오류가 발생했습니다."); // 일반적인 예외 처리
    }
    return false; // 실패시 false 반환
  }

  @Override
  public Optional<User> findById(UUID id){
    if (id == null) {
      return Optional.empty();  // id가 null이면 빈 Optional을 반환
    }
    for (User usr : data){
      if(usr.getId().equals(id)){
        return Optional.of(usr);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<User> findAll(){ return data; }

  @Override
  public boolean updateOne(UUID id, String name, int age, Gender gender) {
    if (id == null) {
      System.out.println("회원 수정 실패");
      return false;
    }
    for (User usr : data) {
      if (usr.getId().equals(id)) {
        usr.update(name, age, gender); // data는 상수이므로 data.update(name, topic) 하면 안됨.
        return true;
      }
    }
    System.out.println("회원 수정 실패");
    return false;
  }

  @Override
  public boolean deleteOne(UUID id){
    if (id == null) {
      System.out.println("회원 삭제 실패");
      return false;
    }
    for (Iterator<User> itr = data.iterator(); itr.hasNext();) {
      User usr = itr.next();
      if (usr.getId().equals(id)) {
        itr.remove();
        return true;
      }
    }
    System.out.println("회원 삭제 실패");
    return false;
  }
}