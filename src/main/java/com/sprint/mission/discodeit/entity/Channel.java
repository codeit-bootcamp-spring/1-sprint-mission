package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "channels")
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity {

  //채널명
  private String name;

  //채널 종류 - 음성, 텍스트
  private ChannelCategory channelCategory;

  //채널 공개 여부
  private ChannelType type;

  //채널 설명
  private String description;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> users = new ArrayList<>();


  public Channel(String name, ChannelType type, ChannelCategory channelCategory,
      String description) {
    this.name = name;
    this.channelCategory = channelCategory;
    this.type = type;
    this.description = description;
  }
}
