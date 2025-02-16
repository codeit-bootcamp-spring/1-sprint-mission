package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserRepository implements UserRepository {
  private final List<UserDto> data;
  public FileUserRepository(SerializationUtil<UserDto> util) {
    this.data = util.loadData(); // 이 부분, filePath 매개변수 이해 잘 안됨
  }

  @Override
  public boolean save(UserDto user){
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
  public Optional<UserDto> findById(UUID id){
    if (id == null) {
      return Optional.empty();  // id가 null이면 빈 Optional을 반환
    }
    for (UserDto usr : data){
      if(usr.user().getId().equals(id)){
        return Optional.of(usr);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<UserDto> findAll(){ return data; }

  @Override
  public boolean updateOne(UUID id, String name, int age, Gender gender) {
    if (id == null) {
      System.out.println("회원 수정 실패");
      return false;
    }
    for (UserDto usr : data) {
      if (usr.user().getId().equals(id)) {
        usr.user().update(name, age, gender); // data는 상수이므로 data.update(name, topic) 하면 안됨.
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
    for (Iterator<UserDto> itr = data.iterator(); itr.hasNext();) {
      UserDto usr = itr.next();
      if (usr.user().getId().equals(id)) {
        itr.remove();
        return true;
      }
    }
    System.out.println("회원 삭제 실패");
    return false;
  }
}