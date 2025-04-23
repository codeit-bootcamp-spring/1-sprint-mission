package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Message extends BaseUpdatableEntity {                  // 메시지 (게시물)

  @Column(name = "content", columnDefinition = "text", nullable = false)
  private String content;   // 메시지 내용

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid")
  private Channel channel;  // 메시지 소속 채널

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", columnDefinition = "uuid")
  private User author;    // 메시지 작성자

  @BatchSize(size = 100)    // 여러 개의 SELECT 쿼리들을 하나의 IN 쿼리로 만들어줌(최대 100개) - N+1 문제 완화
  // 고아 객체 자동 삭제, 메시지 삭제 시 첨부파일 함께 삭제
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinTable(
      name = "message_attachments",   // 조인 테이블
      joinColumns = @JoinColumn(name = "message_id"),   // 현재 엔티티의 FK
      inverseJoinColumns = @JoinColumn(name = "attachment_id")  // 연결될 엔티티의 FK
  )
  private List<BinaryContent> attachments;

  // 메시지 수정
  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }
}