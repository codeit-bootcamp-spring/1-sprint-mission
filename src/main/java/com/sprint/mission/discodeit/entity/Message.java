package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

  @Column
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MessageAttachment> attachments = new ArrayList<>();

  public Message(String content, Channel channel, User author) {
    super();
    this.content = content;
    this.channel = channel;
    this.author = author;
  }

  public void update(String newContent) {
    this.content = newContent;
  }

  /**
   * attachment 파일 추가
   *
   * @param attachment
   */
  public void addAttachment(BinaryContent attachment) {
    MessageAttachment messageAttachment = new MessageAttachment(this, attachment);
    this.attachments.add(messageAttachment);
  }

  /**
   * attachment 파일 제거
   *
   * @param attachment
   */
  public void removeAttachment(BinaryContent attachment) {
    this.attachments.removeIf(ma -> ma.getAttachment().equals(attachment));
  }
}
