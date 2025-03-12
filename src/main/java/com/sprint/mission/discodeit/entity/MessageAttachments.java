package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "message_attachments")
@NoArgsConstructor
public class MessageAttachments {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;


  @ManyToOne
  @JoinColumn(name = "message_id")
  private Message message;

  @ManyToOne
  @JoinColumn(name = "attachment_id")
  private BinaryContent content;

}
