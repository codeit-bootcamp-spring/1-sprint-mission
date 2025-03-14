package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseUpdateEntity {

  private String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
  private List<BinaryContent> attachments;

  public Message(String text, User author, Channel channel) {
    this.text = text;
    this.author = author;
    this.channel = channel;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public void updateText(String text) {
    this.text = text;
    this.updatedAt = Instant.now();
  }
}
