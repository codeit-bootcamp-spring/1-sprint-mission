package com.sprint.mission.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"content", "author", "channel"}, callSuper = true)
@ToString(of = "content")
@Getter
@Schema(description = "메시지 엔티티")
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  private String content;

  @ManyToOne(fetch = LAZY, cascade = REMOVE)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne(fetch = LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private User author;

  // 설계도에서 OneToMany 관계를 JoinTable로 설계하도록 되어있어서...
  // 이런 구조에서 Message삭제 시 binaryContent자동 삭제는 구현 못했습니다(수동으로 메서드 만들어서 해야될까요)
  // MessageCascadeTest 파일에서 테스트 실패
  @OneToMany(fetch = LAZY, cascade = REMOVE, orphanRemoval = true)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> messageAttachments = new ArrayList<>();

  public Message update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
    return this;
  }

  public Message(String content, Channel channel, User author) {
    this.content = content;
    this.channel = channel;
    this.author = author;
  }

  public void addAttachment(BinaryContent attachment) {
    messageAttachments.add(attachment);
  }
}
