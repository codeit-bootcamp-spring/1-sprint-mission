package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import lombok.Locked.Read;


/**
 * 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
 * <p>
 * User 마다 여러 개의 ReadStatus를 가질 수 있다고 상정합니다. 하나의 채널에 하나의 ReadStatus를 가집니다.
 * <p>
 * ! ReadStatus lastMessageReadTime 시간 이후의 createdAt된 Messages 는 읽지 않은 메세지이다. !
 **/

@Entity
@Table(name = "read_statuses")
@Getter
@AllArgsConstructor
@Builder
public class ReadStatus extends BaseUpdatableEntity {

  @Column(nullable = false)
  private Instant lastReadAt; // 마지막으로 읽은 메시지의 시각

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "channel_id")
  private Channel channel;

  // JPA용 기본 생성자, JPA만 접근할 수 있도록 protected 접근자 설정
  protected ReadStatus() {
  }

  public void updateLastMessageReadAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
  }
}