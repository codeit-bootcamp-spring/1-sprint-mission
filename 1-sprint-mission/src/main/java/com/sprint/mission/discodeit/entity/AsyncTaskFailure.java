package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AsyncTaskFailure {

  @Id
  @GeneratedValue
  private Long id;

  private UUID contentId;
  private String reason;
  private String requestId;
  private LocalDateTime occurredAt;
}
