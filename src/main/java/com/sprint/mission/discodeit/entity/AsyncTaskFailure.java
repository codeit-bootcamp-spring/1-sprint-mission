package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsyncTaskFailure {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String taskName;

  private String requestId;

  private String failureReason;

  public AsyncTaskFailure(String taskName, String requestId, String failureReason) {
    this.taskName = taskName;
    this.requestId = requestId;
    this.failureReason = failureReason;
  }
}
