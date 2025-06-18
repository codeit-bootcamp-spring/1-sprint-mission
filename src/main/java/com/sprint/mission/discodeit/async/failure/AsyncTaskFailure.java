package com.sprint.mission.discodeit.async.failure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "async_task_failure")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsyncTaskFailure {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "task_name")
  private String taskName;

  @Column(name = "request_id")
  private String requestId;

  @Column(name = "failure_reason")
  private String failureReason;

  @Column(name = "failed_at")
  private Instant failedAt;

  @Builder
  private AsyncTaskFailure(String taskName, String requestId, String failureReason) {
    this.taskName = taskName;
    this.requestId = requestId;
    this.failureReason = failureReason;
    this.failedAt = Instant.now();
  }
}
