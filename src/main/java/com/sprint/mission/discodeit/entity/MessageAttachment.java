package com.sprint.mission.discodeit.entity;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "message_attachments")
@IdClass(MessageAttachmentId.class)
public class MessageAttachment {

  @Id
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "message_id", nullable = false)
  private Message message;

  @Id
  @Column(name = "attachment_id", nullable = false)
  private UUID attachmentId;

  public MessageAttachment(Message message, UUID attachmentId) {
    this.message = message;
    this.attachmentId = attachmentId;
  }

  public UUID getAttachmentId() {
    return attachmentId;
  }
}