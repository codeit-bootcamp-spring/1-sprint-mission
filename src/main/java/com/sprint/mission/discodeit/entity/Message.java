package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="message")
public class Message extends BaseUpdateEntity {

  private static final long serialVersionUID = 1L;

  private String content;
  //
  @JoinColumn(name="channel_id")
  private UUID channelId;
  @JoinColumn(name="author_id")
  private UUID author;

  @ElementCollection
  @CollectionTable( joinColumns = @JoinColumn(name = "binary_content_id"))
  @Column(name = "attachment_uuid")
  private List<UUID> attachments;


  public Message(String content, UUID channelId, UUID author, List<UUID> attachments) {
    //
    this.content = content;
    this.channelId = channelId;
    this.author = author;
    this.attachments = attachments;
  }
  protected Message(){

  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }
}
