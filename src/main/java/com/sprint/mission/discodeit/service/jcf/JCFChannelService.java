package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
  private final List<Channel> data;
  // Channel 형의 리스트 data를 생성하는 순간, Channel의 객체인 channel이 생성된 것임! 리스트 data는 channel들의 집합.
  public JCFChannelService() {
    this.data = new ArrayList<>();
  }

  @Override
  public boolean createChannel(Channel channel) {
    try {
      if (channel == null) {
        throw new IllegalArgumentException("채널이 null일 수 없습니다.");
      }
      return data.add(channel); // 성공하면 true 반환
    } catch(IllegalArgumentException e) {
        System.out.println("채널 생성 실패: " + e.getMessage());
    } catch(Exception e) {
        System.out.println("알 수 없는 오류가 발생했습니다."); // 일반적인 예외 처리
    }
    return false; // 실패시 false 반환
}

  @Override
  public Optional<Channel> readChannel(UUID id) {
    return data.stream()
        .filter(channel -> channel.getId().equals(id)) // Channel 객체의 리스트인 data의 각 요소(Channel 객체)를 channel이라 두겠어. = channel 변수가 Channel 객체를 참조. 그 다음 ~~.
        .findFirst();
  }

  @Override
  public List<Channel> readAllChannels() {
    return new ArrayList<>(data);
  }

  @Override
  // isPresent()는 boolean, ifPresent()는 void 타입
  public void updateChannel(UUID id, String name, String topic) {
    Optional<Channel> optionalChannel = readChannel(id); // readChannel(id).isPresent; 이렇게 바로 불가능.

    if (optionalChannel.isPresent()){ // readChannel(id)의 반환값이 Optional<Channel>이기 때문에 null 값일 수도 있음. 이걸 대비하는 조건문.
      Channel channel = optionalChannel.get();
      channel.update(name, topic);
    }else {
      System.out.println("채널 수정 실패");
    }
  }
  // readChannel(id).ifPresent(channel -> channel.update(name, topic);


  @Override
  public void deleteChannel(UUID id) {
    Optional<Channel> optionalChannel = readChannel(id);

    if (optionalChannel.isPresent()){
      Channel channel = optionalChannel.get();
      channel.delete(id); // delete 구현 해줘야 함
    }else {
      System.out.println("채널 삭제 실패");
    }
  }
  // data.removeIf(channel -> channel.getId().equals(id));
}
