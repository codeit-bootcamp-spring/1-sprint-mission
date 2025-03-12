package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;

import java.util.UUID;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "message_attachments", joinColumns = @JoinColumn(name = "message_id"))
  @Column(name = "attachment_id")
  private List<UUID> attachmentIds;

  public Message(String content, Channel channel, User author, List<UUID> attachmentIds) {
    this.content = content;
    this.channel = channel;
    this.author = author;
    this.attachmentIds = attachmentIds;
  }

  public void update(String content) {
    if (content != null && !content.equals(this.content)) {
      this.content = content;
    }
  }
}
