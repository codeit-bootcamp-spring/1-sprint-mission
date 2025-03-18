package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "messages")
public class Message extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L; //직렬화 버전

  @Column(columnDefinition = "TEXT")
  private String content; //메세지내용

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private User author;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "message_id") // Message에서 외래 키를 관리
  private List<BinaryContent> attachments;

  //생성자
  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    super();
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
