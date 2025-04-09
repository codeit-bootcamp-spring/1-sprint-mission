package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "read_statuses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "channel_id"
        })
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
//사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", columnDefinition = "uuid")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid")
  private Channel channel;

  @Column(columnDefinition = "timestamp with time zone", nullable = false)
  private Instant lastReadAt; // 마지막으로 읽은 시간

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  //마지막으로 읽은 시간 업데이트 및 읽은 상태 변경
  public void updateLastReadAt(Instant newLastReadAt) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
      updateTimestamp();
    }
  }
}
