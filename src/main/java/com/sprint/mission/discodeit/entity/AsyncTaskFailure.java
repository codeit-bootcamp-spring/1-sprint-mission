package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class AsyncTaskFailure extends BaseEntity {

  private String taskName;
  private String requestId;
  private String failureReason;

  public AsyncTaskFailure(String taskName, String requestId, String failureReason) {
    this.taskName = taskName;
    this.requestId = requestId;
    this.failureReason = failureReason;
  }
}
