package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "channels")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Channel extends BaseUpdatableEntity implements Serializable {

  private static final Long serialVersionUID = 1L;

  @Column(name = "name")
  private String channelName;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private ChannelType type;

  @Column(name = "description")
  private String description;


  //updateName
  public void updateName(String name) {
    this.channelName = name;
    super.update();
  }


}
