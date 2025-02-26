package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;

  // TODO 질문 : @RequiredArgsConstructor가 final 필드를 생성자의 매개변수로 포함하도록 강제하는데,
  // TODO 질문 : id와 createdAt은 명시적으로 초기화되지 않아서 NullPointerException이 발생할 가능성이 높음.
  // TODO 질문 : => 여기서 UUID.randomUUID()와 System.currentTimeMillis()로 초기화해주는 것이 일반적인 패턴..............?
  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;

  private String name;
  private String topic;
  private final ChannelType type;

  // 생성자 롬복으로 하려고 했는데, 그러면 private channel 생성 시 name, topic이 null인 게 롬복을 위한 "@NonNull자"랑 충돌이 되니까 없애고 그냥 생성자 명시해줌
  public Channel(String name, String topic, ChannelType type) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.name = name;
    this.topic = topic;
    this.type = type;
  }


  public void update(String name, String topic) {
    this.name = name;
    this.topic = topic;
    this.updatedAt = Instant.now();
  }

  @Override
  public String toString() {
    return "Channel{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", topic='" + topic + '\'' +
        '}';
  }
}