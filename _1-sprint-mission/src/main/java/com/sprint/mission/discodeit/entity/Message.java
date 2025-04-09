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
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content; //메세지내용

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid")
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", columnDefinition = "uuid")
  private User author;


  @BatchSize(size = 100) // 한 번에 가져오는 데어터 수
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"), //message_attachments 테이블에서 Message 테이블의 외래 키 컬럼 이름을 message_id로 지정
      inverseJoinColumns = @JoinColumn(name = "attachment_id") // message_attachments 테이블에서 BinaryContent 테이블의 외래 키 컬럼 이름을 attachment_id로 지정
  )
  private List<BinaryContent> attachments = new ArrayList<>();

  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    // 검증
    if (content == null || content.trim().isEmpty() || content.length() > 500) {
      throw new IllegalArgumentException("메시지 내용은 1~500자 사이여야 합니다.");
    }
    if (channel.getId() == null) {
      throw new IllegalArgumentException("채널 ID는 필수입니다.");
    }
    if (author.getId() == null) {
      throw new IllegalArgumentException("작성자 ID는 필수입니다.");
    }

    this.content = content;
    this.channel = channel;
    this.author = author;
    this.attachments = attachments;
  }

  // update 메소드
  public void updateContent(String newContent) {
    if (newContent != null && !newContent.trim().isEmpty() && !newContent.equals(this.content)) {
      this.content = newContent;
      updateTimestamp();
    }
  }
}
