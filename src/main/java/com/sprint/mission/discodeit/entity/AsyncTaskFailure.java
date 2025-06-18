package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "async_task_failures")
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsyncTaskFailure extends BaseEntity {

    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private UUID requestId;

    @Column(nullable = false, columnDefinition = "text")
    private String failureReason;

    public AsyncTaskFailure(String taskName, UUID requestId, String failureReason) {
        this.taskName = taskName;
        this.requestId = requestId;
        this.failureReason = failureReason;
    }
}
