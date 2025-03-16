package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "channels")
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private ChannelType type;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.createdAt = Instant.now();
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      this.updatedAt = Instant.now();
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      this.updatedAt = Instant.now();
    }
  }
}
