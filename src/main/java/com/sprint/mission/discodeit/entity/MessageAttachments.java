package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "message_attachments", uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_message_attachment",
        columnNames = {"message_id", "attachment_id"})})
@NoArgsConstructor
public class MessageAttachments extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "message_id")
  private Message message;

  @ManyToOne
  @JoinColumn(name = "attachment_id")
  private BinaryContent content;

}
