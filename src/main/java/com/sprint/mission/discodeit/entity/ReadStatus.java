package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "read-statuses")
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  // 채널 하나에 여러개의 readStatus 가능
  // 부모:채널 채널과 연관관계 끊기면 readStatus 다 삭제돼도 가능 아니야?
  @JoinColumn(name = "channel_id", unique = true)
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;


  public void update(Instant newLastReadAt) {
    boolean anyValueUpdated = false;
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}
