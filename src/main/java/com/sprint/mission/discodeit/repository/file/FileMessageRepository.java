package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
  private final List<Message> data;
  public FileMessageRepository(SerializationUtil<Message> util){
    this.data = util.loadData();
  }

  @Override
  public boolean save(Message message){
    try {
      if (message == null) {
        throw new IllegalArgumentException("메세지가 null일 수 없습니다.");
      }
      return data.add(message); // 성공하면 true 반환
    } catch(IllegalArgumentException e) {
      System.out.println("메세지 생성 실패: " + e.getMessage());
    } catch(Exception e) {
      System.out.println("알 수 없는 오류가 발생했습니다."); // 일반적인 예외 처리
    }
    return false; // 실패시 false 반환
  }

  @Override
  public Optional<Message> findById(UUID id){
    if (id == null) {
      return Optional.empty();  // id가 null이면 빈 Optional을 반환
    }
    for (Message msg : data){
      if(msg.getId().equals(id)){
        return Optional.of(msg);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<Message> findAll(){ return data; }

  @Override
  public boolean updateOne(UUID id, String content, UUID authorId) {
    if (id == null) {
      System.out.println("메세지 수정 실패");
      return false;
    }
    for (Message msg : data) {
      if (msg.getId().equals(id)) {
        msg.update(content); // data는 상수이므로 data.update(name, topic) 하면 안됨.
        return true;
      }
    }
    System.out.println("메세지 수정 실패");
    return false;
  }

  @Override
  public boolean deleteOne(UUID id){
    if (id == null) {
      System.out.println("메세지 삭제 실패");
      return false;
    }
    for (Iterator<Message> itr = data.iterator(); itr.hasNext();) {
      Message msg = itr.next();
      if (msg.getId().equals(id)) {
        itr.remove();
        return true;
      }
    }
    System.out.println("메세지 삭제 실패");
    return false;
  }
}
