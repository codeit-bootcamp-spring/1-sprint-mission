package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.enity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@Getter
public class Message extends BaseUpdatableEntity {

  private String content;
  @ManyToOne
  @JoinColumn(name = "author_id")
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private User author;
  @ManyToOne
  @JoinColumn(name = "channel_id")
  private Channel channel;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments;

  public Message(String content, User author, Channel channel, List<BinaryContent> attachments) {
    super();
    this.content = content;
    this.author = author;
    this.channel = channel;
    this.attachments = attachments;
  }


  public void update(String content) {
    if (content != null && !content.equals(this.content)) {
      this.content = content;
    }
  }

  @Override
  public String toString() {
    return "Message{" +
        "id='" + this.getId() + '\'' +
        ", content='" + this.content + '\'' +
        '}';
  }

  // 객체를 UUID로 비교하기 위해
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Message message = (Message) obj;
    return Objects.equals(this.getId(), message.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

}
