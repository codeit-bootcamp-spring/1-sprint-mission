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

  @Column(columnDefinition = "text", nullable = false)
  private String content;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid")
  private Channel channel;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "author_id", columnDefinition = "uuid")
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private User author;

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

  public Message(String content, Channel channel, User author, List<BinaryContent> messageAttachments) {
    this.content = content;
    this.channel = channel;
    this.author = author;
    this.messageAttachments = messageAttachments;
  }

}
