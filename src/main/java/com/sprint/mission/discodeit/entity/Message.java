/*
package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity */
/*implements Serializable*//*
 {

  @Column(nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "channel_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_message_channel"))
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_message_author"))
  private User author;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "message_id")
  private List<BinaryContent> attachments = new ArrayList<>();

  protected Message() {
    super();
  }

  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    // super(); // 부모의 생성자 호출하여 ID 생성 없어도 호출이 가능하나 명시적으로 작성
    this.content = content;
    this.channel = channel;
    this.author = author;

    if (attachments != null) {
      this.attachments = attachments;
    }

    // 양방향 관계 설정
    if (channel != null) {
      channel.addMessage(this);
    }
    if (author != null) {
      author.addMessage(this);
    }
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

  // 첨부파일 관리를 위한 헬퍼 메서드
  public void addAttachment(BinaryContent attachment) {
    if (attachment != null && !this.attachments.contains(attachment)) {
      this.attachments.add(attachment);
    }
  }

  public void removeAttachment(BinaryContent attachment) {
    this.attachments.remove(attachment);
  }
}
*/
package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

  @Column(columnDefinition = "text", nullable = false)
  private String content;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid")
  private Channel channel;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", columnDefinition = "uuid")
  private User author;
  @BatchSize(size = 100)
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments = new ArrayList<>();

  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    this.channel = channel;
    this.content = content;
    this.author = author;
    this.attachments = attachments;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }
}
