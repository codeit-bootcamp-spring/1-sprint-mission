package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
  // 1. 자료구조 상수 선언 2. 생성자로 객체 생성
  private final List<ChannelDto> data; // Channel 형의 리스트 data를 생성하는 순간, Channel의 객체인 channel이 생성된 것임! 리스트 data는 channel들의 집합
  public JCFChannelRepository() {
    this.data = new ArrayList<>();
  }

  @Override
  public boolean save(ChannelDto channelDto){
    try {
      if (channelDto == null) {
        throw new IllegalArgumentException("채널이 null일 수 없습니다.");
      }
      return data.add(channelDto); // 성공하면 true 반환
    } catch(IllegalArgumentException e) {
      System.out.println("채널 생성 실패: " + e.getMessage());
    } catch(Exception e) {
      System.out.println("알 수 없는 오류가 발생했습니다."); // 일반적인 예외 처리
    }
    return false; // 실패시 false 반환
  }

  @Override
  public Optional<Channel> findById(UUID id){
    if (id == null) {
      return Optional.empty();  // id가 null이면 빈 Optional을 반환
    }
    for (ChannelDto ch : data){
      if(ch.channel().getId().equals(id)){
        return Optional.of(ch.channel());
      }
    }
    return Optional.empty();
  }

  @Override
  public List<ChannelDto> findAll(){ return data; }

  // Optional로도 한 번..?
  @Override
  public boolean updateOne(UUID id, String name, String topic) {
    if (id == null) {
      System.out.println("채널 수정 실패");
      return false;
    }
    for (ChannelDto ch : data) {
      if (ch.channel().getId().equals(id)) {
        ch.channel().update(name, topic); // data는 상수이므로 data.update(name, topic) 하면 안됨.
        return true;
      }
    }
    System.out.println("채널 수정 실패");
    return false;
  }

  @Override
  public boolean deleteOne(UUID id){
    if (id == null) {
      System.out.println("채널 삭제 실패");
      return false;
    }
    for (Iterator<ChannelDto> itr = data.iterator(); itr.hasNext();) {
      ChannelDto ch = itr.next();
      if (ch.channel().getId().equals(id)) {
        itr.remove();
        return true;
      }
    }
    System.out.println("채널 삭제 실패");
    return false;
  }
}
