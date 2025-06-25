package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "async_task_failures")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsyncTaskFailure extends BaseEntity {

    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private String requestId;

    @Column(nullable = false, columnDefinition = "text")
    private String failureReason;

    public AsyncTaskFailure(String taskName, String requestId, String failureReason) {
        this.taskName = taskName;
        this.requestId = requestId;
        this.failureReason = failureReason;
    }
} 