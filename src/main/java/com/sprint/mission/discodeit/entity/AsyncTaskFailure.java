package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "async_task_failures")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AsyncTaskFailure extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String taskName;

    @Column(length = 50)
    private String requestId;

    @Column(length = 1000, nullable = false)
    private String failureReason;
}
