package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "role")
@Getter
public class Role extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String name;

  public Role() {
  }

  public Role(String name) {
    this.name = name;
  }
}
