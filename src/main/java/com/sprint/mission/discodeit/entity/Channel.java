package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(length = 100)
  private String name;
  @Column(length = 500)
  private String description;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private ChannelType type;

  public enum ChannelType {
    PUBLIC,
    PRIVATE
  }


  public void update(String name, String description) {
    this.name = name;
    this.description = description;
    this.updatedAt = Instant.now();
  }
}