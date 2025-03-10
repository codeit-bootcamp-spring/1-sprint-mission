package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "messages")  //
@Getter
@Setter
@NoArgsConstructor
public class Message extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = true)
  private User author;

  @Column(columnDefinition = "TEXT")
  private String content;

  @ManyToMany
  @JoinTable(
          name = "message_attachments",
          joinColumns = @JoinColumn(name = "message_id"),
          inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private Set<BinaryContent> attachments = new HashSet<>();


  public Message(Channel channel, User author, String content, Set<BinaryContent> attachments) {
    this.channel = channel;
    this.author = author;
    this.content = content;
    this.attachments = attachments;
  }


  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      setUpdatedAtNow();
    }
  }
}
